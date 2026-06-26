package org.example.billingsystem.mapper;

import org.example.billingsystem.dtoObject.CustomerRequestDTO;
import org.example.billingsystem.dtoObject.CustomerResponseDTO;
import org.example.billingsystem.dtoObject.InvoiceResponseDTO;
import org.example.billingsystem.model.Customer;
import org.example.billingsystem.model.Invoice;
import org.example.billingsystem.service.InvoiceService;

import java.time.LocalDate;

public class CustomerMapper {
    public static Customer toEntity(CustomerRequestDTO dto){
        Customer customer=new Customer();
        customer.setName(dto.getName());
        customer.setEmail(dto.getEmail());
        customer.setPhoneNo(dto.getPhoneNo());
        customer.setBillDueDate(dto.getBillDueDate());
        customer.setUnitsConsumed(dto.getUnitsConsumed());
        customer.setUsername(dto.getUsername());
        customer.setPassword(dto.getPassword());
        return customer;
    }

    public static CustomerResponseDTO toResponseDto(Customer customer, Invoice invoice){
        CustomerResponseDTO dto=new CustomerResponseDTO();
        dto.setName(customer.getName());
        dto.setUsername(customer.getUsername());
        dto.setId(customer.getId());
        dto.setBillDueDate(customer.getBillDueDate());
        dto.setUnitsConsumed(customer.getUnitsConsumed());
        return dto;
    }

    public static CustomerResponseDTO toResponseDto(Customer customer) {

        CustomerResponseDTO dto = new CustomerResponseDTO();

        dto.setId(customer.getId());
        dto.setName(customer.getName());
        dto.setUsername(customer.getUsername());
        dto.setUnitsConsumed(customer.getUnitsConsumed());
        dto.setBillDueDate(customer.getBillDueDate());

        return dto;
    }
}
