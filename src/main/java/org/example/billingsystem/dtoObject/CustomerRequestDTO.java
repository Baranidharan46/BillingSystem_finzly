package org.example.billingsystem.dtoObject;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


import java.time.LocalDate;
@Data

public class CustomerRequestDTO {

    @NotBlank(message = "name Cannot Be Blank")
    private String name;
    @NotBlank(message = "Email Should Not Be Blank")
    @Email(message = "Email Need To Be In Correct Format")
    private String email;
    @NotBlank(message = "Phone No Should Not Be Null")
    private String phoneNo;
    @NotNull
    @Min(value = 10)
    private Double unitsConsumed;
    @NotNull
    private LocalDate billDueDate;
    @NotBlank
    private String password;
    @NotBlank
    private String username;
}
