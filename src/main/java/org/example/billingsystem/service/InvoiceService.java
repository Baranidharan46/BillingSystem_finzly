package org.example.billingsystem.service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.example.billingsystem.exception.CustomerNotFoundException;
import org.example.billingsystem.exception.InvoiceNotFoundException;
import org.example.billingsystem.model.Customer;
import org.example.billingsystem.model.Invoice;
import org.example.billingsystem.status.PaymentStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;

@Service
public class InvoiceService {
    @Autowired
    CustomerService customerService;

    HashMap<String, Invoice>invoices=new HashMap<>();

    public Invoice generateInvoice(String customerId,boolean isOnlinePayment,boolean isBeforeDueDate){
        Customer getCustomerId=customerService.getById(customerId);
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
        Invoice invoice=new Invoice("Inv"+customerId,customerId,unitConsumed,baseAmount,discount,finalAmount,getCustomerId.getBillDueDate(),PaymentStatus.UNPAID);
        invoices.put(customerId,invoice);
        return invoice;

    }


    public Invoice getInvoiceByCustomerId(String customerId){
        customerService.getById(customerId);
        for (Invoice s:invoices.values()) {
            if (s.getCustomerId().equals(customerId)) {
                return s;
            }
        }
        throw new InvoiceNotFoundException("Customer With this Id"+customerId+" Is Not Found");
    }
}
