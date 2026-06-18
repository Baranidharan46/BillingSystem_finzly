package org.example.billingsystem.dtoObject;
import lombok.Getter;
import lombok.Setter;
import org.example.billingsystem.status.PaymentMethod;
import org.example.billingsystem.status.PaymentStatus;

import java.time.LocalDate;

@Getter
@Setter
public class InvoiceResponseDTO {



    private Long invoiceId;
    private Long customerId;
    private String name;
    private Double baseAmount;
    private Double discount;
    private Double unitsConsumed;
    private Double finalAmount;
    private LocalDate dueDate;
    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;

}
