package org.example.billingsystem.controller;

import org.example.billingsystem.model.Customer;
import org.example.billingsystem.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("v1/customer")
public class CustomerController {
    @Autowired
    CustomerService customerService;

    @PostMapping
    public ResponseEntity<Customer> addCustomer(@RequestBody Customer customer){
        Customer saved=customerService.addCustomer(customer);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getById(@PathVariable long id){
        Customer getById=customerService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(getById);
    }

    @GetMapping
    public ResponseEntity <Collection<Customer>> getAll(){
        Collection<Customer> getAll= customerService.getAllCustomers();
        return ResponseEntity.status(HttpStatus.OK).body(getAll);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Customer> updateAccount(@PathVariable Long id,@RequestBody Customer customer){
        Customer updateAcc= customerService.updateAccount(id, customer);
        return ResponseEntity.status(HttpStatus.OK).body(updateAcc);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long id){
        customerService.deleteAccount(id);
        return ResponseEntity.noContent().build();
    }

}
