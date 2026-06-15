package org.example.billingsystem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.billingsystem.status.PaymentStatus;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table (name = "invoices")

public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long invoiceId;
    private Long customerId;
    private double unitsConsumed;
    private double baseAmount;
    private double discount;
    private double finalAmount;
    private LocalDate dueDate;
    private PaymentStatus paymentStatus;

}
