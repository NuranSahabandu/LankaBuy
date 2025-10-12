package com.project66.eshoppingstore.Repository;

import com.project66.eshoppingstore.entity.MarketingStaff;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MarketingStaffRepository extends JpaRepository<MarketingStaff, String> {
    Optional<MarketingStaff> findByUserName(String userName);
}
