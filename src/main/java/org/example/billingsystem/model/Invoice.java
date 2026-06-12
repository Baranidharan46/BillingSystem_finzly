package org.example.billingsystem.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.billingsystem.status.PaymentStatus;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class Invoice {

    private String invoiceId;
    private String customerId;
    private double unitsConsumed;
    private double baseAmount;
    private double discount;
    private double finalAmount;
    private LocalDate dueDate;
    private PaymentStatus paymentStatus;
}
