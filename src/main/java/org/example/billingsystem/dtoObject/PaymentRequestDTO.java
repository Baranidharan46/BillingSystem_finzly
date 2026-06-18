package org.example.billingsystem.dtoObject;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.example.billingsystem.status.PaymentMethod;

public class PaymentRequestDTO {

    @NotNull(message = "customer Id Cannot Be Null")
    private Long customerId;
    @NotNull(message = "Amount Cannot Be Null")
    private Double amount;
    @NotNull(message = "Enter the Correct PayMent Method")
    private PaymentMethod paymentMethod;

}
