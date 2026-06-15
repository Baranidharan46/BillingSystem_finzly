package org.example.billingsystem.controller;

import org.example.billingsystem.model.Payment;
import org.example.billingsystem.service.PaymentService;
import org.example.billingsystem.status.PaymentMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    PaymentService paymentService;


    @PostMapping("/{customerId}/{invoiceId}")
    public ResponseEntity<Payment> recordPayment(@PathVariable Long customerId,@PathVariable Long invoiceId,@RequestParam PaymentMethod paymentMethod){
        Payment payment=paymentService.recordPayment(customerId, invoiceId, paymentMethod);
        return ResponseEntity.status(HttpStatus.CREATED).body(payment);

    }

    @GetMapping("/{customerId}")
    public ResponseEntity <Payment> getPayment(@PathVariable Long customerId){
        Payment payment =paymentService.getTransactionHistory(customerId);
        return ResponseEntity.status(HttpStatus.OK).body(payment);

    }
}
