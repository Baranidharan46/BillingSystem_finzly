package org.example.billingsystem.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "customers")

public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "name Cannot be Blank")
    private String name;
    @NotBlank(message = "email cannot be blank")
    @Email(message = "Should be in Proper Email Format")
    private String email;
    @NotBlank(message = "PhoneNo Not Be Blank")
    private String phoneNo;
    @NotNull(message = "units Consumed Not Be Null")
    private double unitsConsumed;
    @NotNull(message = "due Not be Null")
    private LocalDate billDueDate;
}
