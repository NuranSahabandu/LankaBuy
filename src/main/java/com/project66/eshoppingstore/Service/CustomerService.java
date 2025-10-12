package com.project66.eshoppingstore.Service;

import com.project66.eshoppingstore.entity.Customer;

public interface CustomerService {
    String registerCustomer(Customer customer);
    Customer loginCustomer(String username, String password);
    Customer findCustomerById(String customerId);
    Customer findCustomerByUserName(String username);
    boolean isUsernameExists(String username);
    boolean isEmailExists(String email);
}
