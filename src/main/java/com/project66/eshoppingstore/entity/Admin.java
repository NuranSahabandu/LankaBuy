package com.project66.eshoppingstore.entity;

import jakarta.persistence.Entity;

@Entity
public class Admin extends User {

    public Admin() {
        super();
    }

    public Admin(String userID, String userName, String email, String password, String phoneNumber, String address) {
        super(userID, userName, email, password, phoneNumber, address);
    }


}
