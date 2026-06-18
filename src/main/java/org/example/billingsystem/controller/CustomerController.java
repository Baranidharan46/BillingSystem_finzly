package org.example.billingsystem.controller;

import jakarta.validation.Valid;
import org.example.billingsystem.dtoObject.CustomerRequestDTO;
import org.example.billingsystem.dtoObject.CustomerResponseDTO;
import org.example.billingsystem.mapper.CustomerMapper;
import org.example.billingsystem.model.Customer;
import org.example.billingsystem.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/customer")
public class CustomerController {
    @Autowired
    CustomerService customerService;

    @PostMapping
    public ResponseEntity<Customer> addCustomer(@Valid @RequestBody CustomerRequestDTO dto){
        Customer customer= CustomerMapper.toEntity(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(customerService.addCustomer(customer));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> getById(@PathVariable long id){
        CustomerResponseDTO customer=customerService.getCustomerById(id);
        return ResponseEntity.status(HttpStatus.OK).body(customer);
    }

    @GetMapping
    public ResponseEntity <Page<Customer>> getAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
        Page<Customer> getAll= customerService.getAllCustomers(page,size);
        return ResponseEntity.status(HttpStatus.OK).body(getAll);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Customer> updateAccount(@PathVariable Long id,@Valid @RequestBody Customer customer){
        Customer updateAcc= customerService.updateAccount(id, customer);
        return ResponseEntity.status(HttpStatus.OK).body(updateAcc);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long id){
        customerService.deleteAccount(id);
        return ResponseEntity.noContent().build();
    }

}
