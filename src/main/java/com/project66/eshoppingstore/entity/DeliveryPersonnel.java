package com.project66.eshoppingstore.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "DeliveryPersonnel")
public class DeliveryPersonnel extends User {

    public DeliveryPersonnel() {
        super();
    }

    public DeliveryPersonnel(String userID, String userName, String email, String password, String phoneNumber, String address) {
        super(userID, userName, email, password, phoneNumber, address);
    }

}
