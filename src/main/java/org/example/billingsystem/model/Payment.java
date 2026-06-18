package org.example.billingsystem.model;

import jakarta.persistence.*;
import lombok.*;
import org.example.billingsystem.status.PaymentMethod;
import org.example.billingsystem.status.PaymentStatus;

import java.time.LocalDate;

@Getter
@Setter
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
