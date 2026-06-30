package org.example.billingsystem.service;

import jakarta.validation.constraints.NotNull;
import org.example.billingsystem.dtoObject.InvoiceRequestDTO;
import org.example.billingsystem.dtoObject.InvoiceResponseDTO;
import org.example.billingsystem.exception.InvoiceNotFoundException;
import org.example.billingsystem.mapper.InvoiceMapper;
import org.example.billingsystem.model.Customer;
import org.example.billingsystem.model.Invoice;
import org.example.billingsystem.repository.InvoiceRepository;
import org.example.billingsystem.status.PaymentStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class InvoiceService {

    private static final Logger log = LoggerFactory.getLogger(InvoiceService.class);

    private final InvoiceRepository invoiceRepository;
    private final CustomerService customerService;

    public InvoiceService(CustomerService customerService,InvoiceRepository invoiceRepository){
        this.customerService=customerService;
        this.invoiceRepository=invoiceRepository;
    }



    public InvoiceResponseDTO generateInvoice(InvoiceRequestDTO dto,boolean isBeforeDueDate,boolean isOnlinePayment){
        log.info("Generating invoice for customerId={} units={} beforeDueDate={} onlinePayment={}",
                dto.getCustomerId(), dto.getUnitsConsumed(), isBeforeDueDate, isOnlinePayment);
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


        Invoice invoice = new Invoice();
        invoice.setName(getCustomerName);
        invoice.setCustomerId(getCustomerId.getId());
        invoice.setUnitsConsumed(unitConsumed);
        invoice.setPaymentStatus(PaymentStatus.UNPAID);
        invoice.setDiscount(discount);
        invoice.setBaseAmount(baseAmount);
        invoice.setDueDate(LocalDate.now());
        invoice.setFinalAmount(finalAmount);
        invoice.setPaymentMethod(null);
        invoice.setPaymentDate(null);
        invoiceRepository.save(invoice);
        log.info("Invoice generated: invoiceId={} customerId={} baseAmount={} discount={} finalAmount={}",
                invoice.getInvoiceId(), invoice.getCustomerId(), baseAmount, discount, finalAmount);
        return InvoiceMapper.toResponseDTO(invoice);

    }


    public InvoiceResponseDTO getInvoiceByCustomerId(Long customerId){
        log.debug("Fetching invoice for customerId={}", customerId);
        Invoice invoice=invoiceRepository.findByCustomerId(customerId)
                .orElseThrow(()->{
                    log.warn("No invoice found for customerId={}", customerId);
                    return new InvoiceNotFoundException("Invoice For This "+customerId+" Is Not Found");
                });
        return InvoiceMapper.toResponseDTO(invoice);
    }
}
