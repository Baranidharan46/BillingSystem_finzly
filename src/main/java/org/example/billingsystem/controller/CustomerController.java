package org.example.billingsystem.controller;

import jakarta.validation.Valid;
import org.example.billingsystem.dtoObject.CustomerLogin;
import org.example.billingsystem.dtoObject.CustomerRequestDTO;
import org.example.billingsystem.dtoObject.CustomerResponseDTO;
import org.example.billingsystem.mapper.CustomerMapper;
import org.example.billingsystem.model.Customer;
import org.example.billingsystem.service.CustomerService;
import org.example.billingsystem.security.JwtService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/customer")
public class CustomerController {

    CustomerService customerService;
    AuthenticationManager authenticationManager;
    JwtService jwtService;

    public CustomerController(CustomerService customerService,AuthenticationManager authenticationManager,JwtService jwtService){
        this.customerService=customerService;
        this.authenticationManager=authenticationManager;
        this.jwtService=jwtService;
    }

    @PostMapping
    public ResponseEntity<Customer> addCustomer(@Valid @RequestBody CustomerRequestDTO dto){
        Customer customer= CustomerMapper.toEntity(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(customerService.addCustomer(customer));
    }
    @PostMapping("/login")
    public String loginCustomer(@Valid @RequestBody CustomerLogin login){
        Authentication authentication=authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login.getUsername(),login.getPassword()));
        if(authentication.isAuthenticated())
            return jwtService.generateToken(login.getUsername());
        else
            return "Failed";
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> getById(@Valid @PathVariable long id){
        CustomerResponseDTO customer=customerService.getCustomerById(id);
        return ResponseEntity.status(HttpStatus.OK).body(customer);
    }

    @GetMapping
    public ResponseEntity <Page<CustomerResponseDTO>> getAll(@Valid @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
        Page<CustomerResponseDTO> getAll= customerService.getAllCustomers(page,size);
        return ResponseEntity.status(HttpStatus.OK).body(getAll);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> updateAccount(@Valid @PathVariable Long id,@Valid @RequestBody Customer customer){
        Customer updateAcc= customerService.updateAccount(id, customer);
        return ResponseEntity.status(HttpStatus.OK).body(CustomerMapper.toResponseDto(updateAcc));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@Valid @PathVariable Long id){
        customerService.deleteAccount(id);
        return ResponseEntity.noContent().build();
    }

}
