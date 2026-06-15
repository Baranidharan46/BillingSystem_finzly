package org.example.billingsystem.service;


import org.example.billingsystem.exception.PaymentNotFoundException;
import org.example.billingsystem.model.Invoice;
import org.example.billingsystem.model.Payment;
import org.example.billingsystem.status.PaymentStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class PaymentService {

    @Autowired
    InvoiceService invoiceService;

    private HashMap<String, Payment> paymentHashMap=new HashMap<>();

    public Payment recordPayment(String customerId,String invoiceId,String paymentMethod){

        Invoice invoice = invoiceService.getInvoiceByCustomerId(customerId);
        Payment payment=new Payment("PAY"+customerId,customerId,invoiceId, invoice.getFinalAmount(),paymentMethod,LocalDate.now(),PaymentStatus.PAID);
        invoice.setPaymentStatus(PaymentStatus.PAID);
        paymentHashMap.put(payment.getPaymentId(),payment);
        return payment;
    }
    public List<Payment> getTransactionHistory(String customerId){
        List<Payment>payments=new ArrayList<>();
        for (Payment s: paymentHashMap.values()){
            if (s.getCustomerId().equals(customerId)){
                payments.add(s);
            }
        }if(payments.isEmpty()){
            throw new PaymentNotFoundException("Payment With This Id: "+customerId+" is Not Found");
        }
        return payments;
    }

}
