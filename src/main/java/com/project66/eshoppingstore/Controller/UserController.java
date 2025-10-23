package com.project66.eshoppingstore.Controller;

import com.project66.eshoppingstore.Service.UserService;
import com.project66.eshoppingstore.entity.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Register a new user
     */
    @PostMapping("/register")
    public Map<String, Object> registerUser(@RequestBody User user) {
        String result = userService.registerUser(user);

        if ("Registration successful!".equals(result)) {
            return Map.of(
                    "success", true,
                    "message", result
            );
        } else {
            return Map.of(
                    "success", false,
                    "message", result
            );
        }
    }

    /**
     * Login user
     */
    @PostMapping("/login")
    public Map<String, Object> loginUser(@RequestBody Map<String, String> loginData, HttpSession session) {
        String usernameOrEmail = loginData.get("usernameOrEmail");
        String password = loginData.get("password");

        User user = userService.loginUser(usernameOrEmail, password);

        if (user != null) {
            // Store user in session
            session.setAttribute("loggedInUser", user);
            session.setAttribute("userId", user.getId());
            session.setAttribute("username", user.getUsername());
            session.setAttribute("userRole", user.getRole().toString());

            return Map.of(
                    "success", true,
                    "message", "Login successful!",
                    "user", Map.of(
                            "id", user.getId(),
                            "username", user.getUsername(),
                            "fullName", user.getFullName(),
                            "email", user.getEmail(),
                            "role", user.getRole().toString()
                    )
            );
        } else {
            return Map.of(
                    "success", false,
                    "message", "Invalid username/email or password!"
            );
        }
    }

    /**
     * Logout user
     */
    @PostMapping("/logout")
    public Map<String, Object> logoutUser(HttpSession session) {
        session.invalidate();
        return Map.of(
                "success", true,
                "message", "Logged out successfully!"
        );
    }

    /**
     * Get current logged-in user
     */
    @GetMapping("/current")
    public Map<String, Object> getCurrentUser(HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");

        if (user != null) {
            return Map.of(
                    "success", true,
                    "user", Map.of(
                            "id", user.getId(),
                            "username", user.getUsername(),
                            "fullName", user.getFullName(),
                            "email", user.getEmail(),
                            "role", user.getRole().toString()
                    )
            );
        } else {
            return Map.of(
                    "success", false,
                    "message", "No user logged in"
            );
        }
    }

    /**
     * Check if user is logged in
     */
    @GetMapping("/check-login")
    public Map<String, Object> checkLogin(HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        return Map.of(
                "loggedIn", user != null,
                "username", user != null ? user.getUsername() : "",
                "role", user != null ? user.getRole().toString() : ""
        );
    }

    /**
     * Get user profile
     */
    @GetMapping("/{userId}")
    public User getUserProfile(@PathVariable Long userId) {
        return userService.findUserById(userId);
    }













    /**
     * Get current user's profile
     */
    @GetMapping("/profile")
    public Map<String, Object> getCurrentUserProfile(HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");

        if (user != null) {
            return Map.of(
                    "success", true,
                    "user", Map.of(
                            "id", user.getId(),
                            "username", user.getUsername(),
                            "email", user.getEmail(),
                            "fullName", user.getFullName(),
                            "phoneNumber", user.getPhoneNumber() != null ? user.getPhoneNumber() : "",
                            "address", user.getAddress() != null ? user.getAddress() : ""
                    )
            );
        } else {
            return Map.of(
                    "success", false,
                    "message", "Please log in to view profile"
            );
        }
    }

    /**
     * Update current user's profile
     */
    @PutMapping("/profile")
    public Map<String, Object> updateProfile(@RequestBody User updatedUser, HttpSession session) {
        User currentUser = (User) session.getAttribute("loggedInUser");

        if (currentUser == null) {
            return Map.of(
                    "success", false,
                    "message", "Please log in to update profile"
            );
        }

        try {
            // Get the user from database to ensure we have the latest data
            User userToUpdate = userService.findUserById(currentUser.getId());

            if (userToUpdate == null) {
                return Map.of(
                        "success", false,
                        "message", "User not found"
                );
            }

            // Update only allowed fields (don't allow username/email changes for simplicity)
            userToUpdate.setFullName(updatedUser.getFullName());
            userToUpdate.setPhoneNumber(updatedUser.getPhoneNumber());
            userToUpdate.setAddress(updatedUser.getAddress());

            // Update password only if provided
            if (updatedUser.getPassword() != null && !updatedUser.getPassword().trim().isEmpty()) {
                if (updatedUser.getPassword().length() < 6) {
                    return Map.of(
                            "success", false,
                            "message", "Password must be at least 6 characters"
                    );
                }
                userToUpdate.setPassword(updatedUser.getPassword());
            }

            String result = userService.updateUser(userToUpdate);

            if ("Profile updated successfully!".equals(result)) {
                // Update session with new data
                session.setAttribute("loggedInUser", userToUpdate);

                return Map.of(
                        "success", true,
                        "message", "Profile updated successfully!"
                );
            } else {
                return Map.of(
                        "success", false,
                        "message", result
                );
            }

        } catch (Exception e) {
            return Map.of(
                    "success", false,
                    "message", "Failed to update profile: " + e.getMessage()
            );
        }
    }

    /**
     * Delete current user's profile
     */
    @DeleteMapping("/profile")
    public Map<String, Object> deleteProfile(HttpSession session) {
        User currentUser = (User) session.getAttribute("loggedInUser");

        if (currentUser == null) {
            return Map.of(
                    "success", false,
                    "message", "Please log in to delete profile"
            );
        }

        try {
            userService.deleteUser(currentUser.getId());

            // Invalidate session after deletion
            session.invalidate();

            return Map.of(
                    "success", true,
                    "message", "Profile deleted successfully"
            );

        } catch (Exception e) {
            return Map.of(
                    "success", false,
                    "message", "Failed to delete profile: " + e.getMessage()
            );
        }
    }


}