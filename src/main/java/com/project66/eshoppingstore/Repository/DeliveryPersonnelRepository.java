package com.project66.eshoppingstore.Repository;

import com.project66.eshoppingstore.entity.DeliveryPersonnel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DeliveryPersonnelRepository extends JpaRepository<DeliveryPersonnel, String> {
    Optional<DeliveryPersonnel> findByUserName(String userName);
}
