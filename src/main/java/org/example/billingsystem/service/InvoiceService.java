package org.example.billingsystem.service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.example.billingsystem.exception.CustomerNotFoundException;
import org.example.billingsystem.exception.InvoiceNotFoundException;
import org.example.billingsystem.model.Customer;
import org.example.billingsystem.model.Invoice;
import org.example.billingsystem.repository.CustomerRepository;
import org.example.billingsystem.repository.InvoiceRepository;
import org.example.billingsystem.status.PaymentStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Optional;

@Service
public class InvoiceService {
    @Autowired
    CustomerService customerService;
    @Autowired
    InvoiceRepository invoiceRepository;
    @Autowired
    CustomerRepository customerRepository;



    public Invoice generateInvoice(Long customerId,boolean isOnlinePayment,boolean isBeforeDueDate){
        Customer getCustomerId=customerService.findById(customerId);
        double unitConsumed=getCustomerId.getUnitsConsumed();


        double baseAmount=unitConsumed*41.50;
        double finalAmount=baseAmount;
        double discount=0;
        if (isBeforeDueDate==true){
            discount+=baseAmount*0.05;
            finalAmount=baseAmount-discount;
        }
        if (isOnlinePayment==true){
            discount+=baseAmount*0.05;
            finalAmount=baseAmount-discount;
        }
        Invoice invoice=new Invoice(null,customerId,unitConsumed,baseAmount,discount,finalAmount,getCustomerId.getBillDueDate(),PaymentStatus.UNPAID);
        invoiceRepository.save(invoice);
        return invoice;

    }


    public Invoice getInvoiceByCustomerId(Long customerId){
        customerService.findById(customerId);
        return invoiceRepository.findById(customerId)
                .orElseThrow(()->new InvoiceNotFoundException("Invoice For This "+customerId+" Is Not Found"));
    }
}
