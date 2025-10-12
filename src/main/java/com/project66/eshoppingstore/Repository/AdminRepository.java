package com.project66.eshoppingstore.Repository;

import com.project66.eshoppingstore.entity.Admin;
import com.project66.eshoppingstore.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Customer,String> {
    Optional<Admin> findByUserName(String userName);
}
