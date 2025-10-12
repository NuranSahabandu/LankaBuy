package com.project66.eshoppingstore.Service.impl;

import com.project66.eshoppingstore.Repository.CustomerRepository;
import com.project66.eshoppingstore.Service.CustomerService;
import com.project66.eshoppingstore.entity.Customer;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CustomerServiceImpl implements CustomerService {

    CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public String registerCustomer(Customer customer) {
        // Generate unique customer ID
        customer.setUserId(UUID.randomUUID().toString());
        customerRepository.save(customer);
        return "Customer registered successfully!";
    }

    @Override
    public Customer loginCustomer(String username, String password) {
        // Find customer by username
        Customer customer = customerRepository.findByUserName(username)
                .orElse(null);

        // Check if customer exists and password matches
        if (customer != null && customer.getPassword().equals(password)) {
            return customer; //Login Success
        }

        return null; //Login failed
    }

    @Override
    public Customer findCustomerById(String customerId) {
        return customerRepository.findById(customerId)
                .orElse(null);
    }

    @Override
    public Customer findCustomerByUserName(String username) {
        return customerRepository.findByUserName(username)
                .orElse(null);
    }

    @Override
    public boolean isUsernameExists(String username) {
        return customerRepository.existsByUserName(username);
    }

    @Override
    public boolean isEmailExists(String email) {
        return customerRepository.existsByEmail(email);
    }

}
