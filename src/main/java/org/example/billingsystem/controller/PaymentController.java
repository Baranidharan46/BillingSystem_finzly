package org.example.billingsystem.controller;

import org.example.billingsystem.dtoObject.PaymentResponseDTO;
import org.example.billingsystem.model.Payment;
import org.example.billingsystem.service.PaymentService;
import org.example.billingsystem.status.PaymentMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    PaymentService paymentService;


    @PostMapping("/{customerId}")
    public ResponseEntity<PaymentResponseDTO> recordPayment(@PathVariable Long customerId,@RequestParam PaymentMethod paymentMethod){
        PaymentResponseDTO payment=paymentService.recordPayment(customerId,paymentMethod);
        return ResponseEntity.status(HttpStatus.CREATED).body(payment);

    }

    @GetMapping("/{customerId}")
    public ResponseEntity <PaymentResponseDTO> getPayment(@PathVariable Long customerId){
        PaymentResponseDTO dto =paymentService.getTransactionHistory(customerId);
        return ResponseEntity.status(HttpStatus.OK).body(dto);

    }
}
