package com.project66.eshoppingstore.Controller;

import com.project66.eshoppingstore.Service.OrderService;
import com.project66.eshoppingstore.entity.Order;
import com.project66.eshoppingstore.entity.OrderItem;
import com.project66.eshoppingstore.entity.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/create")
    public Map<String, Object> createOrder(@RequestBody Map<String, String> orderData, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");

        if (user == null) {
            return Map.of("success", false, "message", "Please log in to place order");
        }

        String customerName = orderData.get("customerName");
        String customerEmail = orderData.get("customerEmail");
        String shippingAddress = orderData.get("shippingAddress");

        String result = orderService.createOrderFromCart(user.getId(), customerName, customerEmail, shippingAddress);

        if (result.startsWith("Order created successfully")) {
            return Map.of("success", true, "message", result);
        } else {
            return Map.of("success", false, "message", result);
        }
    }

    @GetMapping("/my-orders")
    public Map<String, Object> getUserOrders(HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");

        if (user == null) {
            return Map.of("success", false, "message", "Please log in to view orders");
        }

        List<Order> orders = orderService.getUserOrders(user.getId());

        return Map.of(
                "success", true,
                "orders", orders
        );
    }

    @GetMapping("/{orderId}")
    public Map<String, Object> getOrderDetails(@PathVariable Long orderId, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");

        if (user == null) {
            return Map.of("success", false, "message", "Please log in");
        }

        Order order = orderService.getOrderById(orderId);

        if (order == null || !order.getUserId().equals(user.getId())) {
            return Map.of("success", false, "message", "Order not found");
        }

        List<OrderItem> orderItems = orderService.getOrderItems(orderId);

        return Map.of(
                "success", true,
                "order", order,
                "orderItems", orderItems
        );
    }

    // Admin endpoints
    @GetMapping("/all")
    public Map<String, Object> getAllOrders(HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");

        if (user == null || !"ADMIN".equals(user.getRole().toString())) {
            return Map.of("success", false, "message", "Admin access required");
        }

        List<Order> orders = orderService.getAllOrders();

        return Map.of(
                "success", true,
                "orders", orders
        );
    }

    @PutMapping("/{orderId}/status")
    public Map<String, Object> updateOrderStatus(@PathVariable Long orderId,
                                                 @RequestBody Map<String, String> statusData,
                                                 HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");

        if (user == null || !"ADMIN".equals(user.getRole().toString())) {
            return Map.of("success", false, "message", "Admin access required");
        }

        String status = statusData.get("status");
        String result = orderService.updateOrderStatus(orderId, status);

        return Map.of("success", true, "message", result);
    }
}