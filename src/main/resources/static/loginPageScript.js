
// ==================== LOGIN FUNCTIONALITY ====================

// Handle form submission
document.getElementById('loginForm').addEventListener('submit', async function(e) {
    e.preventDefault();

    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;
    const rememberMe = document.getElementById('rememberMe').checked;

    // Validate inputs
    if (!email || !password) {
        showAlert('Please fill in all required fields', 'error');
        return;
    }

    // Show loading
    showLoading(true);

    try {
        // Simulate API call (replace with your actual login API)
        await simulateLogin(email, password, rememberMe);

        // Success
        showAlert('Login successful! Redirecting...', 'success');

        // Redirect after 2 seconds
        setTimeout(() => {
            window.location.href = 'dashboard.html'; // Replace with your main page
        }, 2000);

    } catch (error) {
        // Error handling
        showAlert(error.message || 'Login failed. Please try again.', 'error');
    } finally {
        showLoading(false);
    }
});

// Simulate login API call (replace with real backend integration)
async function simulateLogin(email, password, rememberMe) {
    return new Promise((resolve, reject) => {
        setTimeout(() => {
            // Demo validation (replace with real authentication)
            if (email === 'demo@lankabuy.com' && password === 'demo123') {
                resolve({ success: true, user: { email, rememberMe } });
            } else {
                reject(new Error('Invalid email or password'));
            }
        }, 2000); // Simulate network delay
    });
}

// ==================== UI FUNCTIONS ====================

// Toggle password visibility
function togglePassword() {
    const passwordInput = document.getElementById('password');
    const toggleBtn = document.querySelector('.password-toggle');

    if (passwordInput.type === 'password') {
        passwordInput.type = 'text';
        toggleBtn.textContent = '🙈';
    } else {
        passwordInput.type = 'password';
        toggleBtn.textContent = '👁️';
    }
}

// Show/hide loading spinner
function showLoading(isLoading) {
    const loadingSpinner = document.getElementById('loadingSpinner');
    const submitBtn = document.querySelector('.login-submit-btn');

    if (isLoading) {
        loadingSpinner.style.display = 'inline-block';
        submitBtn.style.opacity = '0.7';
        submitBtn.disabled = true;
    } else {
        loadingSpinner.style.display = 'none';
        submitBtn.style.opacity = '1';
        submitBtn.disabled = false;
    }
}

// Show alert messages
function showAlert(message, type) {
    // Remove existing alerts
    const existingAlert = document.querySelector('.alert');
    if (existingAlert) {
        existingAlert.remove();
    }

    // Create new alert
    const alert = document.createElement('div');
    alert.className = `alert ${type}`;
    alert.style.cssText = `
                position: fixed;
                top: 20px;
                right: 20px;
                background: ${type === 'success' ? '#4CAF50' : '#f44336'};
                color: white;
                padding: 15px 25px;
                border-radius: 8px;
                box-shadow: 0 4px 12px rgba(0,0,0,0.2);
                z-index: 1000;
                font-weight: 600;
                animation: slideIn 0.3s ease;
            `;
    alert.textContent = message;

    document.body.appendChild(alert);

    // Auto remove after 5 seconds
    setTimeout(() => {
        if (alert.parentNode) {
            alert.remove();
        }
    }, 5000);
}

// ==================== SOCIAL LOGIN FUNCTIONS ====================

function loginWithGoogle() {
    showAlert('Google login integration coming soon!', 'info');
    // Implement Google OAuth here
}

function loginWithFacebook() {
    showAlert('Facebook login integration coming soon!', 'info');
    // Implement Facebook OAuth here
}

// ==================== NAVIGATION FUNCTIONS ====================

function showRegisterForm() {
    showAlert('Registration page coming soon!', 'info');
    // Redirect to registration page or show registration form
}

// ==================== DEMO HELPER ====================

// Add demo credentials hint
window.addEventListener('load', function() {
    setTimeout(() => {
        showAlert('Demo: Use email "demo@lankabuy.com" and password "demo123"', 'info');
    }, 1000);
});

// Add CSS animation for alerts
const style = document.createElement('style');
style.textContent = `
            @keyframes slideIn {
                from {
                    transform: translateX(100%);
                    opacity: 0;
                }
                to {
                    transform: translateX(0);
                    opacity: 1;
                }
            }
        `;
document.head.appendChild(style);
