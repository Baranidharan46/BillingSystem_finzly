package org.example.billingsystem.service;
import org.example.billingsystem.config.UserPrincipal;
import org.example.billingsystem.model.Customer;
import org.example.billingsystem.repository.CustomerRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class MyUserDetailService implements UserDetailsService {

    CustomerRepository customerRepository;

    public MyUserDetailService(CustomerRepository customerRepository){
        this.customerRepository=customerRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("=== Loading user: " + username);
        Optional<Customer> customer = customerRepository.findByUsername(username);
        System.out.println("=== Found: " + customer.isPresent());
        if (customer.isEmpty()) {
            throw new UsernameNotFoundException("Username is Not Found");
        }
        System.out.println("=== Password from DB: " + customer.get().getPassword());
        return new UserPrincipal(customer.get());
    }
}
