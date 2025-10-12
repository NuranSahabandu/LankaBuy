package com.project66.eshoppingstore.Repository;

import com.project66.eshoppingstore.entity.SupportStaff;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SupportStaffRepository extends JpaRepository<SupportStaff, String> {
    Optional<SupportStaff> findByUserName(String userName);
}
