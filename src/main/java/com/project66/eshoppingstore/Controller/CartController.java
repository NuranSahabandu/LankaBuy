package com.project66.eshoppingstore.Controller;

import com.project66.eshoppingstore.Service.CartService;
import com.project66.eshoppingstore.entity.CartItem;
import com.project66.eshoppingstore.entity.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/add")
    public Map<String, Object> addToCart(@RequestBody Map<String, Object> requestData, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");

        if (user == null) {
            return Map.of("success", false, "message", "Please log in to add items to cart");
        }

        try {
            String productId = (String) requestData.get("productId");
            Integer quantity = (Integer) requestData.get("quantity");

            if (quantity == null || quantity <= 0) {
                quantity = 1;
            }

            String result = cartService.addToCart(user.getId(), productId, quantity);

            return Map.of(
                    "success", true,
                    "message", result
            );
        } catch (Exception e) {
            return Map.of("success", false, "message", "Failed to add to cart");
        }
    }

    @GetMapping("/items")
    public Map<String, Object> getCartItems(HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");

        if (user == null) {
            return Map.of("success", false, "message", "Please log in to view cart");
        }

        List<CartItem> cartItems = cartService.getCartItems(user.getId());
        String total = cartService.calculateCartTotal(user.getId());

        return Map.of(
                "success", true,
                "cartItems", cartItems,
                "total", total
        );
    }

    @PutMapping("/update")
    public Map<String, Object> updateCartItem(@RequestBody Map<String, Object> requestData, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");

        if (user == null) {
            return Map.of("success", false, "message", "Please log in");
        }

        String productId = (String) requestData.get("productId");
        Integer quantity = (Integer) requestData.get("quantity");

        String result = cartService.updateCartItem(user.getId(), productId, quantity);

        return Map.of("success", true, "message", result);
    }

    @DeleteMapping("/remove/{productId}")
    public Map<String, Object> removeFromCart(@PathVariable String productId, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");

        if (user == null) {
            return Map.of("success", false, "message", "Please log in");
        }

        String result = cartService.removeFromCart(user.getId(), productId);

        return Map.of("success", true, "message", result);
    }

    @DeleteMapping("/clear")
    public Map<String, Object> clearCart(HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");

        if (user == null) {
            return Map.of("success", false, "message", "Please log in");
        }

        String result = cartService.clearCart(user.getId());

        return Map.of("success", true, "message", result);
    }
}