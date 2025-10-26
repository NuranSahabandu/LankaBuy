package com.project66.eshoppingstore.Service;

import com.project66.eshoppingstore.entity.CartItem;

import java.util.List;

public interface CartService {
    String addToCart(Long userId, String productId, Integer quantity);
    String updateCartItem(Long userId, String productId, Integer quantity);
    String removeFromCart(Long userId, String productId);
    List<CartItem> getCartItems(Long userId);
    String clearCart(Long userId);
    String calculateCartTotal(Long userId);
}
