package org.example.billingsystem.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Customer {
    private String name;
    private String id;
    private String email;
    private String phoneNo;
    private double unitsConsumed;
    private LocalDate billDueDate;
}
