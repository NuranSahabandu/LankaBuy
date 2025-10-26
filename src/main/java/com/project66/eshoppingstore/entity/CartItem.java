package com.project66.eshoppingstore.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "cart_items")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private String productId;
    private Integer quantity;
    private String productName;  // For convenience
    private String productPrice; // Current price when added to cart

    // Constructors
    public CartItem() {}

    public CartItem(Long userId, String productId, Integer quantity, String productName, String productPrice) {
        this.userId = userId;
        this.productId = productId;
        this.quantity = quantity;
        this.productName = productName;
        this.productPrice = productPrice;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public String getProductPrice() { return productPrice; }
    public void setProductPrice(String productPrice) { this.productPrice = productPrice; }
}