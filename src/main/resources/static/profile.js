document.addEventListener('DOMContentLoaded', function() {
    // Check if user is logged in
    Auth.requireLogin().then(isLoggedIn => {
        if (isLoggedIn) {
            loadProfile();
        }
    });

    // Handle form submission
    document.getElementById('profileForm').addEventListener('submit', function(e) {
        e.preventDefault();
        updateProfile();
    });
});

// Load user profile data
async function loadProfile() {
    try {
        const response = await fetch('/users/profile');
        const result = await response.json();

        if (result.success) {
            const user = result.user;

            // Populate form fields
            document.getElementById('username').value = user.username;
            document.getElementById('email').value = user.email;
            document.getElementById('fullName').value = user.fullName;
            document.getElementById('phoneNumber').value = user.phoneNumber || '';
            document.getElementById('address').value = user.address || '';

            // Clear password fields
            document.getElementById('password').value = '';
            document.getElementById('confirmPassword').value = '';

        } else {
            showMessage(result.message, 'error');
            // Redirect to login if not authenticated
            setTimeout(() => {
                window.location.href = 'login.html';
            }, 2000);
        }
    } catch (error) {
        console.error('Error loading profile:', error);
        showMessage('Failed to load profile data', 'error');
    }
}

// Update user profile
async function updateProfile() {
    try {
        // Get form data
        const fullName = document.getElementById('fullName').value.trim();
        const phoneNumber = document.getElementById('phoneNumber').value.trim();
        const address = document.getElementById('address').value.trim();
        const password = document.getElementById('password').value;
        const confirmPassword = document.getElementById('confirmPassword').value;

        // Validate required fields
        if (!fullName) {
            showMessage('Full name is required', 'error');
            return;
        }

        // Validate password if provided
        if (password) {
            if (password.length < 6) {
                showMessage('Password must be at least 6 characters', 'error');
                return;
            }

            if (password !== confirmPassword) {
                showMessage('Passwords do not match', 'error');
                return;
            }
        }

        // Prepare update data
        const updateData = {
            fullName: fullName,
            phoneNumber: phoneNumber,
            address: address
        };

        // Add password only if provided
        if (password) {
            updateData.password = password;
        }

        // Show loading state
        const submitBtn = document.querySelector('.btn-primary');
        const originalText = submitBtn.textContent;
        submitBtn.textContent = 'Updating...';
        submitBtn.disabled = true;

        // Send update request
        const response = await fetch('/users/profile', {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(updateData)
        });

        const result = await response.json();

        // Reset button
        submitBtn.textContent = originalText;
        submitBtn.disabled = false;

        if (result.success) {
            showMessage('Profile updated successfully!', 'success');

            // Clear password fields
            document.getElementById('password').value = '';
            document.getElementById('confirmPassword').value = '';

            // Update navigation to reflect changes
            Auth.updateNavigation();

        } else {
            showMessage(result.message, 'error');
        }

    } catch (error) {
        console.error('Error updating profile:', error);
        showMessage('Failed to update profile', 'error');

        // Reset button
        const submitBtn = document.querySelector('.btn-primary');
        submitBtn.textContent = 'Update Profile';
        submitBtn.disabled = false;
    }
}

// Show delete confirmation modal
function confirmDeleteProfile() {
    document.getElementById('deleteModal').classList.add('show');
}

// Close delete confirmation modal
function closeDeleteModal() {
    document.getElementById('deleteModal').classList.remove('show');
}

// Delete user profile
async function deleteProfile() {
    try {
        const response = await fetch('/users/profile', {
            method: 'DELETE'
        });

        const result = await response.json();

        if (result.success) {
            showMessage('Account deleted successfully. Redirecting...', 'success');

            // Close modal
            closeDeleteModal();

            // Redirect to home page after a delay
            setTimeout(() => {
                window.location.href = 'login.html';
            }, 2000);

        } else {
            showMessage(result.message, 'error');
            closeDeleteModal();
        }

    } catch (error) {
        console.error('Error deleting profile:', error);
        showMessage('Failed to delete account', 'error');
        closeDeleteModal();
    }
}

// Show message function
function showMessage(message, type) {
    const messageContainer = document.getElementById('messageContainer');
    const messageContent = document.getElementById('messageContent');

    messageContent.textContent = message;
    messageContent.className = `message-content ${type}`;
    messageContainer.style.display = 'block';

    // Hide message after 5 seconds
    setTimeout(() => {
        messageContainer.style.display = 'none';
    }, 5000);
}

// Close modal when clicking outside
document.getElementById('deleteModal').addEventListener('click', function(e) {
    if (e.target === this) {
        closeDeleteModal();
    }
});