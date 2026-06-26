package org.example.billingsystem.dtoObject;

import lombok.Getter;
import lombok.Setter;
import org.example.billingsystem.status.PaymentMethod;
import org.example.billingsystem.status.PaymentStatus;

import java.time.LocalDate;

@Getter
@Setter
public class PaymentResponseDTO {
    private Long paymentId;
    private String name;
    private Long customerId;
    private Double unitsConsumed;
    private LocalDate paymentDate;
    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;

}
