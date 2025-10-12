package com.project66.eshoppingstore.Controller;

import com.project66.eshoppingstore.Service.CustomerService;
import com.project66.eshoppingstore.entity.Customer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerCustomer(@RequestBody Customer customer) {
        Map<String, Object> response = new HashMap<>();

        // Check if username already exists
        if (customerService.isUsernameExists(customer.getUserName())){
            response.put("success", false);
            response.put("message", "Username already exists");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        // Check if email already exists
        if (customerService.isEmailExists(customer.getEmail())) {
            response.put("success", false);
            response.put("message", "Email already exists");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        // Register customer
        String result = customerService.registerCustomer(customer);

        response.put("success", true);
        response.put("message", result);
        response.put("userId", customer.getUserId());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginCustomer(@RequestBody Map<String, String> credentials) {
        Map<String, Object> response = new HashMap<>();

        String username = credentials.get("username");
        String password = credentials.get("password");

        // Attempt login
        Customer customer = customerService.loginCustomer(username, password);

        if (customer != null) {
            response.put("success", true);
            response.put("message", "Login successful");
            response.put("userId", customer.getUserId());
            response.put("username", customer.getUserName());
            response.put("email", customer.getEmail());
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "Invalid username or password");

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    // Check if email exists (for real-time validation)
    @GetMapping("/check-username/{username}")
    public ResponseEntity<?> checkUsername(@PathVariable String username) {
        Map<String, Object> response = new HashMap<>();
        boolean exists = customerService.isUsernameExists(username);

        response.put("exists", exists);
        return ResponseEntity.ok(response);
    }

    // Get customer profile
    @GetMapping("/{customerId}")
    public ResponseEntity<?> getCustomerProfile(@PathVariable String customerId) {
        Customer customer = customerService.findCustomerById(customerId);

        if (customer != null) {
            // Don't send password to frontend
            customer.setPassword(null);
            return ResponseEntity.ok(customer);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Customer not found");
        }
    }

}
