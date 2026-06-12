package org.example.billingsystem.service;

import org.example.billingsystem.exception.CustomerNotFoundException;
import org.example.billingsystem.model.Customer;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
@Service
public class CustomerService {

    private HashMap<String,Customer>customers=new HashMap<>();

    public Customer addCustomer(Customer customer){
        customers.put(customer.getId(), customer);
        return customer;
    }

    public Customer getById(String id){
        if (customers.get(id)==null){
            throw new CustomerNotFoundException("Student with this "+id+" is Not Found");
        }
        else {
            return customers.get(id);
        }
    }

    public Collection<Customer> getAllCustomers(){
        return customers.values();
    }

    public Customer updateAccount(String id,Customer customer){
        customers.put(id, customer);
        return customer;
    }

    public String deleteAccount(String id) {
        customers.remove(id);
        return "Customer Deleted";
    }
}
