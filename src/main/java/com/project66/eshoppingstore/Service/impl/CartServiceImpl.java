package com.project66.eshoppingstore.Service.impl;

import com.project66.eshoppingstore.Repository.CartRepository;
import com.project66.eshoppingstore.Repository.ProductRepository;
import com.project66.eshoppingstore.Service.CartService;
import com.project66.eshoppingstore.entity.CartItem;
import com.project66.eshoppingstore.entity.Product;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    public CartServiceImpl(CartRepository cartRepository, ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
    }

    @Override
    public String addToCart(Long userId, String productId, Integer quantity) {
        try {
            // Check if product exists
            Optional<Product> productOpt = productRepository.findById(productId);
            if (productOpt.isEmpty()) {
                return "Product not found";
            }

            Product product = productOpt.get();

            // Check if item already exists in cart
            Optional<CartItem> existingItem = cartRepository.findByUserIdAndProductId(userId, productId);

            if (existingItem.isPresent()) {
                // Update quantity
                CartItem cartItem = existingItem.get();
                cartItem.setQuantity(cartItem.getQuantity() + quantity);
                cartRepository.save(cartItem);
                return "Cart updated successfully";
            } else {
                // Add new item
                CartItem cartItem = new CartItem(userId, productId, quantity,
                        product.getProductName(), product.getProductPrice());
                cartRepository.save(cartItem);
                return "Product added to cart";
            }
        } catch (Exception e) {
            return "Failed to add to cart: " + e.getMessage();
        }
    }

    @Override
    public String updateCartItem(Long userId, String productId, Integer quantity) {
        try {
            Optional<CartItem> cartItemOpt = cartRepository.findByUserIdAndProductId(userId, productId);

            if (cartItemOpt.isPresent()) {
                CartItem cartItem = cartItemOpt.get();
                cartItem.setQuantity(quantity);
                cartRepository.save(cartItem);
                return "Cart item updated";
            } else {
                return "Item not found in cart";
            }
        } catch (Exception e) {
            return "Failed to update cart item: " + e.getMessage();
        }
    }

    @Override
    public String removeFromCart(Long userId, String productId) {
        try {
            Optional<CartItem> cartItemOpt = cartRepository.findByUserIdAndProductId(userId, productId);

            if (cartItemOpt.isPresent()) {
                cartRepository.delete(cartItemOpt.get());
                return "Item removed from cart";
            } else {
                return "Item not found in cart";
            }
        } catch (Exception e) {
            return "Failed to remove from cart: " + e.getMessage();
        }
    }

    @Override
    public List<CartItem> getCartItems(Long userId) {
        return cartRepository.findByUserId(userId);
    }

    @Override
    public String clearCart(Long userId) {
        try {
            cartRepository.deleteByUserId(userId);
            return "Cart cleared successfully";
        } catch (Exception e) {
            return "Failed to clear cart: " + e.getMessage();
        }
    }

    @Override
    public String calculateCartTotal(Long userId) {
        try {
            List<CartItem> cartItems = cartRepository.findByUserId(userId);
            double total = 0.0;

            for (CartItem item : cartItems) {
                double price = Double.parseDouble(item.getProductPrice());
                total += price * item.getQuantity();
            }

            return String.format("%.2f", total);
        } catch (Exception e) {
            return "0.00";
        }
    }
}