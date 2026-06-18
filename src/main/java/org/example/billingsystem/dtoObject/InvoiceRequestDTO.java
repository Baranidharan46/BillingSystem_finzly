package org.example.billingsystem.dtoObject;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class InvoiceRequestDTO {

    @NotNull(message = "Customer Id Is Not To Be Null")
    private Long customerId;
    @NotNull(message = "units Consumed Cannot Be Null")
    @Min(value = 25)
    private Double unitsConsumed;
    @NotNull(message = "Due Date Should Be In Proper Format")
    private LocalDate dueDate;

}
