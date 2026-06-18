package org.example.billingsystem.service;


import org.example.billingsystem.dtoObject.InvoiceResponseDTO;
import org.example.billingsystem.dtoObject.PaymentRequestDTO;
import org.example.billingsystem.dtoObject.PaymentResponseDTO;
import org.example.billingsystem.exception.InvoiceNotFoundException;
import org.example.billingsystem.exception.PaymentNotFoundException;
import org.example.billingsystem.mapper.PaymentMapper;
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


    public PaymentResponseDTO recordPayment(Long customerId,PaymentMethod paymentMethod){

        Invoice invoice = invoiceRepository.findByCustomerId(customerId)
                .orElseThrow(()->new InvoiceNotFoundException("Invoice With This CustomerId : "+customerId+" is Not Found"));
        invoice.setPaymentStatus(PaymentStatus.PAID);
        Payment payment=new Payment(null,invoice.getName(),customerId,invoice.getInvoiceId(),invoice.getFinalAmount(), invoice.getUnitsConsumed(), paymentMethod,LocalDate.now(),invoice.getPaymentStatus());
        paymentRepository.save(payment);
        invoiceRepository.save(invoice);
        return PaymentMapper.toResponse(payment);
    }
    public PaymentResponseDTO getTransactionHistory(Long customerId) {
        Payment payment=paymentRepository.findByCustomerId(customerId)
                .orElseThrow(()->new PaymentNotFoundException("Transaction History For This "+customerId+" is Not Found"));
        return PaymentMapper.toResponse(payment);
    }
}
