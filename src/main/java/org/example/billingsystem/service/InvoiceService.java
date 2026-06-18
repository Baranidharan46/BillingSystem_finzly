package org.example.billingsystem.service;

import jakarta.validation.constraints.NotNull;
import org.example.billingsystem.dtoObject.InvoiceRequestDTO;
import org.example.billingsystem.dtoObject.InvoiceResponseDTO;
import org.example.billingsystem.exception.InvoiceNotFoundException;
import org.example.billingsystem.mapper.InvoiceMapper;
import org.example.billingsystem.model.Customer;
import org.example.billingsystem.model.Invoice;
import org.example.billingsystem.repository.CustomerRepository;
import org.example.billingsystem.repository.InvoiceRepository;
import org.example.billingsystem.status.PaymentStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final CustomerService customerService;
    @Autowired
    CustomerRepository customerRepository;

    public InvoiceService(CustomerService customerService,InvoiceRepository invoiceRepository){
        this.customerService=customerService;
        this.invoiceRepository=invoiceRepository;
    }



    public InvoiceResponseDTO generateInvoice(InvoiceRequestDTO dto,boolean isBeforeDueDate,boolean isOnlinePayment){
        Customer getCustomerId=customerService.findById(dto.getCustomerId());
        String getCustomerName= getCustomerId.getName();
        double unitConsumed= dto.getUnitsConsumed();


        double baseAmount=unitConsumed*41.50;
        double finalAmount=baseAmount;
        double discount=0;
        if (isBeforeDueDate==true){
            discount+=baseAmount*0.05;
        }
        if (isOnlinePayment == true) {
            discount += baseAmount * 0.05;
        }
        finalAmount=baseAmount-discount;


        Invoice invoice = new Invoice(null, getCustomerId.getId(),getCustomerName,unitConsumed, baseAmount, discount, finalAmount, dto.getDueDate(), PaymentStatus.UNPAID);
        invoiceRepository.save(invoice);
        return InvoiceMapper.toResponseDTO(invoice);

    }


    public InvoiceResponseDTO getInvoiceByCustomerId(Long customerId){
        Invoice invoice=invoiceRepository.findById(customerId)
                .orElseThrow(()->new InvoiceNotFoundException("Invoice For This "+customerId+" Is Not Found"));
        return InvoiceMapper.toResponseDTO(invoice);
    }
}
