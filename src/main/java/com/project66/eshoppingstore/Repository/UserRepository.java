package com.project66.eshoppingstore.Repository;

import com.project66.eshoppingstore.entity.Customer;
import com.project66.eshoppingstore.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<Customer> findByUserName(String userName);
    boolean existsByUserName(String userName);
}
