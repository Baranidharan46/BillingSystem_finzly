package org.example.billingsystem.service;

import org.example.billingsystem.config.SecurityConfig;
import org.example.billingsystem.dtoObject.CustomerResponseDTO;
import org.example.billingsystem.exception.CustomerNotFoundException;
import org.example.billingsystem.mapper.CustomerMapper;
import org.example.billingsystem.model.Customer;
import org.example.billingsystem.repository.CustomerRepository;
import org.example.billingsystem.repository.InvoiceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerService {

    private static final Logger log = LoggerFactory.getLogger(CustomerService.class);

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
        log.info("Adding new customer username={}", customer.getUsername());
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        customerRepository.save(customer);
        log.info("Customer added: id={}", customer.getId());
        return customer;
    }

    public Customer findById(long id){
       Optional<Customer> customer=customerRepository.findById(id);
       if(customer.isPresent()){
           return customer.get();
       }
       else {
           log.warn("Customer not found: id={}", id);
           throw new CustomerNotFoundException("Customer With This id"+id+" Is Not Found");
       }
    }

    public Page<CustomerResponseDTO> getAllCustomers(int page, int size){
        Pageable pageable= PageRequest.of(page,size);
        Page<Customer>customers=customerRepository.findAll(pageable);
        return customers.map(CustomerMapper::toResponseDto);
    }

    public Customer updateAccount(Long id,Customer customer){
        log.info("Updating customer id={}", id);
        Customer existing=findById(id);

        // Partial update: only overwrite fields that were actually supplied, so a PATCH
        // never wipes the rest of the record.
        if(customer.getName()!=null){
            existing.setName(customer.getName());
        }
        if(customer.getEmail()!=null){
            existing.setEmail(customer.getEmail());
        }
        if(customer.getPhoneNo()!=null){
            existing.setPhoneNo(customer.getPhoneNo());
        }
        if(customer.getUsername()!=null){
            existing.setUsername(customer.getUsername());
        }
        if(customer.getBillDueDate()!=null){
            existing.setBillDueDate(customer.getBillDueDate());
        }
        // unitsConsumed is a primitive double, so a missing value arrives as 0.0; only
        // apply it when a positive value was sent to avoid zeroing the record.
        if(customer.getUnitsConsumed()>0){
            existing.setUnitsConsumed(customer.getUnitsConsumed());
        }
        // Re-encode the password when changed; storing it raw would break BCrypt login.
        if(customer.getPassword()!=null){
            existing.setPassword(passwordEncoder.encode(customer.getPassword()));
        }
        return customerRepository.save(existing);
    }

    public void deleteAccount(Long id) {
        log.info("Deleting customer id={}", id);
        findById(id);
        customerRepository.deleteById(id);
        log.info("Customer deleted: id={}", id);
    }

    public CustomerResponseDTO getCustomerById(Long id){
        Customer customer=customerRepository.findById(id)
                .orElseThrow(()-> {
                    log.warn("Customer not found: id={}", id);
                    return new CustomerNotFoundException("Customer With This Id "+id+" Is Not Found");
                });
        return CustomerMapper.toResponseDto(customer);
    }
}


//List<Customer>customerList=customerRepository.findAll(pageable).getContent()
//        .stream().filter(customer -> customer.getId()>10)
//        .collect(Collectors.toList());
//        return customerList;