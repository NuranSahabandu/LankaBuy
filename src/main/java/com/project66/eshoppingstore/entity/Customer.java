package com.project66.eshoppingstore.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "Customers")
public class Customer extends User {

    public Customer() {
        super();
    }

    public Customer(String userID, String userName, String email, String password, String phoneNumber, String address) {
        super(userID, userName, email, password, phoneNumber, address);
    }



}
