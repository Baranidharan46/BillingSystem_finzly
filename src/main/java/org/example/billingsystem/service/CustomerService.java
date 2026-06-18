package org.example.billingsystem.service;

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
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;

@Service
public class CustomerService {


    CustomerRepository customerRepository;
    InvoiceRepository invoiceRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository, InvoiceRepository invoiceRepository){
        this.customerRepository=customerRepository;
        this.invoiceRepository=invoiceRepository;
    }

    public Customer addCustomer(Customer customer){
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

    public Page<Customer> getAllCustomers(int page, int size){
        Pageable pageable= PageRequest.of(page,size);
        return customerRepository.findAll(pageable);
    }

    public Customer updateAccount(Long id,Customer customer){
        findById(id);
        customer.setId(id);
        return customerRepository.save(customer);

    }

    public String deleteAccount(Long id) {
        findById(id);
        customerRepository.deleteById(id);
        return "Customer Deleted";
    }

    public CustomerResponseDTO getCustomerById(Long id){
        Customer customer=customerRepository.findById(id)
                .orElseThrow(()-> new CustomerNotFoundException("Customer With This Id"+id+"Is Not Found"));
        Invoice invoice=invoiceRepository.findById(id)
                .orElseThrow(()->new InvoiceNotFoundException("Invoice With This "+id+" Is Not Found"));
        return CustomerMapper.toResponseDto(customer,invoice);
    }
}
