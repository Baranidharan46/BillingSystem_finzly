package org.example.billingsystem.service;

import org.example.billingsystem.config.SecurityConfig;
import org.example.billingsystem.dtoObject.CustomerResponseDTO;
import org.example.billingsystem.exception.CustomerNotFoundException;
import org.example.billingsystem.exception.InvoiceNotFoundException;
import org.example.billingsystem.mapper.CustomerMapper;
import org.example.billingsystem.model.Customer;
import org.example.billingsystem.model.Invoice;
import org.example.billingsystem.repository.CustomerRepository;
import org.example.billingsystem.repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerService {


    CustomerRepository customerRepository;
    InvoiceRepository invoiceRepository;
    PasswordEncoder passwordEncoder;

    @Autowired
    public CustomerService(CustomerRepository customerRepository, InvoiceRepository invoiceRepository,PasswordEncoder passwordEncoder){
        this.customerRepository=customerRepository;
        this.invoiceRepository=invoiceRepository;
        this.passwordEncoder=passwordEncoder;
    }

    public Customer addCustomer(Customer customer){
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        customerRepository.save(customer);
        return customer;
    }

    public Customer findById(long id){
       Optional<Customer> customer=customerRepository.findById(id);
       if(customer.isPresent()){
           return customer.get();
       }
       else {
           throw new CustomerNotFoundException("Customer With This id"+id+" Is Not Found");
       }
    }

    public Page<CustomerResponseDTO> getAllCustomers(int page, int size){
        Pageable pageable= PageRequest.of(page,size);
        Page<Customer>customers=customerRepository.findAll(pageable);
        return customers.map(CustomerMapper::toResponseDto);
    }

    public Customer updateAccount(Long id,Customer customer){
        findById(id);
        customer.setId(id);
        return customerRepository.save(customer);
    }

    public void deleteAccount(Long id) {
        findById(id);
        customerRepository.deleteById(id);
    }

    public CustomerResponseDTO getCustomerById(Long id){
        Customer customer=customerRepository.findById(id)
                .orElseThrow(()-> new CustomerNotFoundException("Customer With This Id"+id+"Is Not Found"));
        Invoice invoice=invoiceRepository.findById(id)
                .orElseThrow(()->new InvoiceNotFoundException("Invoice With This "+id+" Is Not Found"));
        return CustomerMapper.toResponseDto(customer,invoice);
    }
}


//List<Customer>customerList=customerRepository.findAll(pageable).getContent()
//        .stream().filter(customer -> customer.getId()>10)
//        .collect(Collectors.toList());
//        return customerList;