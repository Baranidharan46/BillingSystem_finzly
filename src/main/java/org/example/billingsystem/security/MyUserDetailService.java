package org.example.billingsystem.security;
import org.example.billingsystem.model.Customer;
import org.example.billingsystem.repository.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class MyUserDetailService implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(MyUserDetailService.class);

    CustomerRepository customerRepository;

    public MyUserDetailService(CustomerRepository customerRepository){
        this.customerRepository=customerRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("Loading user by username={}", username);
        Optional<Customer> customer = customerRepository.findByUsername(username);
        if (customer.isEmpty()) {
            log.warn("Authentication failed: username={} not found", username);
            throw new UsernameNotFoundException("Username is Not Found");
        }
        return new UserPrincipal(customer.get());
    }
}
