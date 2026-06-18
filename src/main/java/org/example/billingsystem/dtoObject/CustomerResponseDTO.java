package org.example.billingsystem.dtoObject;

import lombok.Getter;
import lombok.Setter;
import org.example.billingsystem.status.PaymentStatus;

import java.time.LocalDate;
@Getter
@Setter
public class CustomerResponseDTO {

    private String name;
    private Long id;
    private Double unitsConsumed;
    private LocalDate billDueDate;
    private PaymentStatus paymentStatus;
    private Double finalAmount;
}
