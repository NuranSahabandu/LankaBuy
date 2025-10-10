
// ==================== REGISTRATION FUNCTIONALITY ====================

let formValidation = {
    firstName: false,
    lastName: false,
    email: false,
    phone: false,
    password: false,
    confirmPassword: false,
    terms: false
};

// Handle form submission
document.getElementById('registerForm').addEventListener('submit', async function(e) {
    e.preventDefault();

    // Validate all fields
    if (!validateAllFields()) {
        showAlert('Please fix the errors and try again', 'error');
        return;
    }

    const formData = getFormData();

    // Show loading
    showLoading(true);

    try {
        // Simulate API call (replace with your actual register API)
        await simulateRegistration(formData);

        // Success
        showAlert('Account created successfully! Please check your email for verification.', 'success');
        showSuccessState();

        // Redirect after 3 seconds
        setTimeout(() => {
            goToLogin();
        }, 3000);

    } catch (error) {
        // Error handling
        showAlert(error.message || 'Registration failed. Please try again.', 'error');
    } finally {
        showLoading(false);
    }
});

// Get form data
function getFormData() {
    return {
        firstName: document.getElementById('firstName').value,
        lastName: document.getElementById('lastName').value,
        email: document.getElementById('email').value,
        phone: document.getElementById('countryCode').value + document.getElementById('phone').value,
        password: document.getElementById('password').value,
        dateOfBirth: document.getElementById('dateOfBirth').value,
        gender: document.getElementById('gender').value
    };
}

// Simulate registration API call (replace with real backend integration)
async function simulateRegistration(formData) {
    return new Promise((resolve, reject) => {
        setTimeout(() => {
            // Demo validation (replace with real backend call)
            if (formData.email === 'test@exists.com') {
                reject(new Error('Email already exists'));
            } else {
                resolve({ success: true, userId: 'new_user_123' });
            }
        }, 2000); // Simulate network delay
    });
}

// ==================== REAL-TIME VALIDATION ====================

// Add event listeners for real-time validation
document.addEventListener('DOMContentLoaded', function() {
    // First Name validation
    document.getElementById('firstName').addEventListener('input', function() {
        validateFirstName();
    });

    // Last Name validation
    document.getElementById('lastName').addEventListener('input', function() {
        validateLastName();
    });

    // Email validation
    document.getElementById('email').addEventListener('input', function() {
        validateEmail();
    });

    // Phone validation
    document.getElementById('phone').addEventListener('input', function() {
        validatePhone();
    });

    // Password validation
    document.getElementById('password').addEventListener('input', function() {
        validatePassword();
        updatePasswordStrength();
    });

    // Confirm password validation
    document.getElementById('confirmPassword').addEventListener('input', function() {
        validateConfirmPassword();
    });

    // Terms checkbox
    document.getElementById('termsCheckbox').addEventListener('change', function() {
        validateTerms();
    });
});

// Validation functions
function validateFirstName() {
    const firstName = document.getElementById('firstName').value;
    const errorDiv = document.getElementById('firstNameError');
    const input = document.getElementById('firstName');

    if (firstName.length < 2) {
        showFieldError(input, errorDiv, 'First name must be at least 2 characters');
        formValidation.firstName = false;
    } else if (!/^[a-zA-Z\s]+$/.test(firstName)) {
        showFieldError(input, errorDiv, 'First name can only contain letters');
        formValidation.firstName = false;
    } else {
        showFieldSuccess(input, errorDiv, '✓ Looks good!');
        formValidation.firstName = true;
    }
}

function validateLastName() {
    const lastName = document.getElementById('lastName').value;
    const errorDiv = document.getElementById('lastNameError');
    const input = document.getElementById('lastName');

    if (lastName.length < 2) {
        showFieldError(input, errorDiv, 'Last name must be at least 2 characters');
        formValidation.lastName = false;
    } else if (!/^[a-zA-Z\s]+$/.test(lastName)) {
        showFieldError(input, errorDiv, 'Last name can only contain letters');
        formValidation.lastName = false;
    } else {
        showFieldSuccess(input, errorDiv, '✓ Looks good!');
        formValidation.lastName = true;
    }
}

function validateEmail() {
    const email = document.getElementById('email').value;
    const errorDiv = document.getElementById('emailError');
    const input = document.getElementById('email');

    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

    if (!emailRegex.test(email)) {
        showFieldError(input, errorDiv, 'Please enter a valid email address');
        formValidation.email = false;
    } else {
        showFieldSuccess(input, errorDiv, '✓ Valid email address');
        formValidation.email = true;
    }
}

function validatePhone() {
    const phone = document.getElementById('phone').value;
    const errorDiv = document.getElementById('phoneError');
    const input = document.getElementById('phone');

    // Sri Lankan phone number validation (basic)
    const phoneRegex = /^[0-9]{9}$/;

    if (!phoneRegex.test(phone.replace(/\s/g, ''))) {
        showFieldError(input, errorDiv, 'Please enter a valid phone number (9 digits)');
        formValidation.phone = false;
    } else {
        showFieldSuccess(input, errorDiv, '✓ Valid phone number');
        formValidation.phone = true;
    }
}

function validatePassword() {
    const password = document.getElementById('password').value;
    const errorDiv = document.getElementById('passwordError');
    const input = document.getElementById('password');

    if (password.length < 8) {
        showFieldError(input, errorDiv, 'Password must be at least 8 characters');
        formValidation.password = false;
    } else if (!/(?=.*[a-z])(?=.*[A-Z])(?=.*\d)/.test(password)) {
        showFieldError(input, errorDiv, 'Password must contain uppercase, lowercase, and numbers');
        formValidation.password = false;
    } else {
        showFieldSuccess(input, errorDiv, '✓ Strong password');
        formValidation.password = true;
    }
}

function validateConfirmPassword() {
    const password = document.getElementById('password').value;
    const confirmPassword = document.getElementById('confirmPassword').value;
    const errorDiv = document.getElementById('confirmPasswordError');
    const input = document.getElementById('confirmPassword');

    if (confirmPassword !== password) {
        showFieldError(input, errorDiv, 'Passwords do not match');
        formValidation.confirmPassword = false;
    } else if (confirmPassword.length > 0) {
        showFieldSuccess(input, errorDiv, '✓ Passwords match');
        formValidation.confirmPassword = true;
    }
}

function validateTerms() {
    const terms = document.getElementById('termsCheckbox').checked;
    formValidation.terms = terms;

    updateRegisterButton();
}

function validateAllFields() {
    validateFirstName();
    validateLastName();
    validateEmail();
    validatePhone();
    validatePassword();
    validateConfirmPassword();
    validateTerms();

    return Object.values(formValidation).every(isValid => isValid);
}