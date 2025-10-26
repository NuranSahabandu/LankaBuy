package com.project66.eshoppingstore.Service;

import com.project66.eshoppingstore.entity.Order;
import com.project66.eshoppingstore.entity.OrderItem;

import java.util.List;

public interface OrderService {
    String createOrderFromCart(Long userId, String customerName, String customerEmail, String shippingAddress);
    Order getOrderById(Long orderId);
    List<Order> getUserOrders(Long userId);
    List<Order> getAllOrders();
    List<OrderItem> getOrderItems(Long orderId);
    String updateOrderStatus(Long orderId, String status);
}