package org.example.billingsystem.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.billingsystem.status.PaymentStatus;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Payment {

    private String paymentId;
    private String customerId;
    private String invoiceId;
    private Double amount;
    private String paymentMethod;
    private LocalDate paymentDate;
    private PaymentStatus paymentStatus;



}
