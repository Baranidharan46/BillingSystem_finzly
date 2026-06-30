package org.example.billingsystem.service;

import org.example.billingsystem.dtoObject.PaymentResponseDTO;
import org.example.billingsystem.exception.InvalidPaymentException;
import org.example.billingsystem.exception.InvoiceNotFoundException;
import org.example.billingsystem.exception.PaymentNotFoundException;
import org.example.billingsystem.mapper.PaymentMapper;
import org.example.billingsystem.model.Invoice;
import org.example.billingsystem.model.Payment;
import org.example.billingsystem.repository.InvoiceRepository;
import org.example.billingsystem.repository.PaymentRepository;
import org.example.billingsystem.status.PaymentMethod;
import org.example.billingsystem.status.PaymentStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;


@Service
public class PaymentService {

    private static final Logger log = LoggerFactory.getLogger(PaymentService.class);

    private final PaymentRepository paymentRepository;
    private final InvoiceRepository invoiceRepository;

    public PaymentService(PaymentRepository paymentRepository,InvoiceRepository invoiceRepository){
        this.invoiceRepository=invoiceRepository;
        this.paymentRepository=paymentRepository;
    }

    public PaymentResponseDTO recordPayment(Long customerId,PaymentMethod paymentMethod,Double amount){
        log.info("Recording {} payment of {} for customerId={}", paymentMethod, amount, customerId);
        Invoice invoice = invoiceRepository.findByCustomerId(customerId)
                .orElseThrow(()->{
                    log.warn("No invoice found for customerId={} while recording payment", customerId);
                    return new InvoiceNotFoundException("Invoice With This CustomerId : "+customerId+" is Not Found");
                });

        // Already-paid guard: prevents double payment (which would also create a second
        // payment row and break the single-result findByCustomerId lookup).
        if (invoice.getPaymentStatus() == PaymentStatus.PAID) {
            log.warn("Invoice for customerId={} is already PAID; rejecting payment", customerId);
            throw new InvalidPaymentException("Invoice for customerId " + customerId + " is already PAID");
        }

        // Exact-amount validation: the bill must be paid in full, no more, no less.
        // Compared with a small tolerance because finalAmount is a double.
        double amountDue = invoice.getFinalAmount();
        if (amount == null || Math.abs(amount - amountDue) > 0.01) {
            log.warn("Payment amount {} does not match amount due {} for customerId={}", amount, amountDue, customerId);
            throw new InvalidPaymentException("Payment amount must equal the amount due of " + amountDue
                    + " (received " + amount + ")");
        }

        invoice.setPaymentStatus(PaymentStatus.PAID);
        invoice.setPaymentMethod(paymentMethod);
        invoice.setPaymentDate(LocalDate.now());
        Payment payment=new Payment(null,invoice.getName(),customerId,invoice.getInvoiceId(),invoice.getFinalAmount(), invoice.getUnitsConsumed(), paymentMethod,LocalDate.now(),invoice.getPaymentStatus());
        invoiceRepository.save(invoice);
        Payment savedPayment = paymentRepository.save(payment);
        log.info("Payment recorded: paymentId={} invoiceId={} customerId={} amount={}",
                savedPayment.getPaymentId(), invoice.getInvoiceId(), customerId, invoice.getFinalAmount());
        return PaymentMapper.toResponse(savedPayment);
    }
    public PaymentResponseDTO getTransactionHistory(Long customerId) {
        log.debug("Fetching transaction history for customerId={}", customerId);
        Payment payment=paymentRepository.findFirstByCustomerIdOrderByPaymentDateDesc(customerId)
                .orElseThrow(()->{
                    log.warn("No transaction history found for customerId={}", customerId);
                    return new PaymentNotFoundException("Transaction History For This "+customerId+" is Not Found");
                });
        return PaymentMapper.toResponse(payment);
    }

    public Page<PaymentResponseDTO> getAllTransactions(int page, int size) {
        log.debug("Fetching all payment transactions page={} size={}", page, size);
        Pageable pageable= PageRequest.of(page,size);
        Page<Payment> payments=paymentRepository.findAll(pageable);
        return payments.map(PaymentMapper::toResponse);
    }

    public Page<PaymentResponseDTO> getTransactionsByCustomer(Long customerId, int page, int size) {
        log.debug("Fetching transactions for customerId={} page={} size={}", customerId, page, size);
        Pageable pageable= PageRequest.of(page,size);
        Page<Payment> payments=paymentRepository.findByCustomerIdOrderByPaymentDateDesc(customerId, pageable);
        return payments.map(PaymentMapper::toResponse);
    }
}
