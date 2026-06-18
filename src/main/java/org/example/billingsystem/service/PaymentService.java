package org.example.billingsystem.service;


import org.example.billingsystem.dtoObject.InvoiceResponseDTO;
import org.example.billingsystem.exception.PaymentNotFoundException;
import org.example.billingsystem.model.Invoice;
import org.example.billingsystem.model.Payment;
import org.example.billingsystem.repository.InvoiceRepository;
import org.example.billingsystem.repository.PaymentRepository;
import org.example.billingsystem.status.PaymentMethod;
import org.example.billingsystem.status.PaymentStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;


@Service
public class PaymentService {

    @Autowired
    InvoiceService invoiceService;
    @Autowired
    PaymentRepository paymentRepository;
    @Autowired
    InvoiceRepository invoiceRepository;


    public Payment recordPayment(Long customerId, Long invoiceId, PaymentMethod paymentMethod){

        Invoice invoice = invoiceRepository.findByCustomerId(customerId)
                .orElseThrow(()->new PaymentNotFoundException("Payment With This "+customerId+" is Not Found"));
        Payment payment=new Payment(null,customerId,invoiceId, invoice.getFinalAmount(),paymentMethod,LocalDate.now(),PaymentStatus.PAID);
        invoice.setPaymentStatus(PaymentStatus.PAID);
        paymentRepository.save(payment);
        invoiceRepository.save(invoice);
        return payment;
    }
    public Payment getTransactionHistory(Long customerId) {
        return paymentRepository.findByCustomerId(customerId)
                .orElseThrow(()->new PaymentNotFoundException("Transaction History For This "+customerId+" is Not Found"));
    }
}
