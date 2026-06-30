package org.example.billingsystem.controller;

import jakarta.validation.Valid;
import org.example.billingsystem.dtoObject.PaymentResponseDTO;
import org.example.billingsystem.service.PaymentService;
import org.example.billingsystem.status.PaymentMethod;
import org.springframework.http.HttpStatus;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/v1/payment")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService){
        this.paymentService=paymentService;
    }


    @PostMapping("/{customerId}")
    public ResponseEntity<PaymentResponseDTO> recordPayment(@Valid @PathVariable Long customerId, @RequestParam PaymentMethod paymentMethod, @RequestParam Double amount){
        PaymentResponseDTO payment=paymentService.recordPayment(customerId,paymentMethod,amount);
        return ResponseEntity.status(HttpStatus.CREATED).body(payment);

    }

    @GetMapping("/{customerId}")
    public ResponseEntity <PaymentResponseDTO> getPayment(@Valid @PathVariable Long customerId){
        PaymentResponseDTO dto =paymentService.getTransactionHistory(customerId);
        return ResponseEntity.status(HttpStatus.OK).body(dto);

    }

    @GetMapping
    public ResponseEntity<Page<PaymentResponseDTO>> getAllPayments(@RequestParam(required = false) Long customerId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
        Page<PaymentResponseDTO> payments;
        if(customerId == null){
            payments = paymentService.getAllTransactions(page,size);
        }
        else{
            payments = paymentService.getTransactionsByCustomer(customerId,page,size);
        }
        return ResponseEntity.status(HttpStatus.OK).body(payments);
    }
}
