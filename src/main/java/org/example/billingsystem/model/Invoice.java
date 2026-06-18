package org.example.billingsystem.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.example.billingsystem.status.PaymentMethod;
import org.example.billingsystem.status.PaymentStatus;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table (name = "invoices")

public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long invoiceId;
    private Long customerId;
    private String name;
    private double unitsConsumed;
    private double baseAmount;
    private double discount;
    private double finalAmount;
    private LocalDate dueDate;
    private PaymentStatus paymentStatus;
    private PaymentMethod paymentMethod;
    private LocalDate paymentDate;

}
