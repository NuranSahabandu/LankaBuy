package com.project66.eshoppingstore.Service.impl;

import com.project66.eshoppingstore.Repository.CartRepository;
import com.project66.eshoppingstore.Repository.OrderRepository;
import com.project66.eshoppingstore.Repository.OrderItemRepository;
import com.project66.eshoppingstore.Service.OrderService;
import com.project66.eshoppingstore.Service.CartService;
import com.project66.eshoppingstore.entity.CartItem;
import com.project66.eshoppingstore.entity.Order;
import com.project66.eshoppingstore.entity.OrderItem;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartService cartService;
    private final CartRepository cartRepository;

    public OrderServiceImpl(OrderRepository orderRepository, OrderItemRepository orderItemRepository,
                            CartService cartService, CartRepository cartRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.cartService = cartService;
        this.cartRepository = cartRepository;
    }

    @Override
    @Transactional
    public String createOrderFromCart(Long userId, String customerName, String customerEmail, String shippingAddress) {
        try {
            // Get cart items
            List<CartItem> cartItems = cartService.getCartItems(userId);

            if (cartItems.isEmpty()) {
                return "Cart is empty";
            }

            // Calculate total
            String totalAmount = cartService.calculateCartTotal(userId);

            // Create order
            Order order = new Order(userId, totalAmount, customerName, customerEmail, shippingAddress);
            Order savedOrder = orderRepository.save(order);

            // Create order items
            for (CartItem cartItem : cartItems) {
                OrderItem orderItem = new OrderItem(
                        savedOrder.getOrderId(),
                        cartItem.getProductId(),
                        cartItem.getProductName(),
                        cartItem.getQuantity(),
                        cartItem.getProductPrice()
                );
                orderItemRepository.save(orderItem);
            }

            // Clear cart
            cartService.clearCart(userId);

            return "Order created successfully. Order ID: " + savedOrder.getOrderId();

        } catch (Exception e) {
            return "Failed to create order: " + e.getMessage();
        }
    }

    @Override
    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElse(null);
    }

    @Override
    public List<Order> getUserOrders(Long userId) {
        return orderRepository.findByUserIdOrderByOrderDateDesc(userId);
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public List<OrderItem> getOrderItems(Long orderId) {
        return orderItemRepository.findByOrderId(orderId);
    }

    @Override
    public String updateOrderStatus(Long orderId, String status) {
        try {
            Order order = orderRepository.findById(orderId).orElse(null);
            if (order != null) {
                order.setStatus(status);
                orderRepository.save(order);
                return "Order status updated successfully";
            } else {
                return "Order not found";
            }
        } catch (Exception e) {
            return "Failed to update order status: " + e.getMessage();
        }
    }
}
