package com.project66.eshoppingstore.Repository;

import com.project66.eshoppingstore.entity.Admin;
import com.project66.eshoppingstore.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer,String> {
    Optional<Customer> findByUserName(String userName);
    Optional<Customer> findByEmail(String email);
    boolean existsByUserName(String userName);
    boolean existsByEmail(String email);
}
