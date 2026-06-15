package org.example.billingsystem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.billingsystem.status.PaymentMethod;
import org.example.billingsystem.status.PaymentStatus;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table (name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;
    private Long customerId;
    private Long invoiceId;
    private Double amount;
    private PaymentMethod paymentMethod;
    private LocalDate paymentDate;
    private PaymentStatus paymentStatus;

}
