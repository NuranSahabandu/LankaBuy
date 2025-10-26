document.addEventListener('DOMContentLoaded', function() {
    // Check if user is logged in
    Auth.requireLogin().then(isLoggedIn => {
        if (isLoggedIn) {
            loadCartItems();
        }
    });

    // Handle checkout form submission
    document.getElementById('checkoutForm').addEventListener('submit', function(e) {
        e.preventDefault();
        placeOrder();
    });
});

// Load cart items
async function loadCartItems() {
    try {
        const response = await fetch('/cart/items');
        const result = await response.json();

        if (result.success) {
            displayCartItems(result.cartItems, result.total);
        } else {
            showMessage(result.message, 'error');
            if (result.message.includes("log in")) {
                setTimeout(() => {
                    window.location.href = 'login.html';
                }, 2000);
            }
        }
    } catch (error) {
        console.error('Error loading cart:', error);
        showMessage('Failed to load cart', 'error');
    }
}

// Display cart items
function displayCartItems(cartItems, total) {
    const container = document.getElementById('cartItems');

    if (cartItems.length === 0) {
        container.innerHTML = `
            <div class="empty-cart">
                <div class="empty-cart-icon">🛒</div>
                <h2>Your cart is empty</h2>
                <p>Add some items to get started!</p>
                <button onclick="window.location.href='lankabuy_browse_products.html'" class="continue-shopping-btn">
                    Start Shopping
                </button>
            </div>
        `;

        // Disable checkout button
        document.getElementById('checkoutBtn').disabled = true;
        document.getElementById('totalItems').textContent = '0';
        document.getElementById('totalAmount').textContent = 'Rs. 0.00';
        return;
    }

    // Build cart items HTML
    let itemsHTML = '';
    cartItems.forEach(item => {
        itemsHTML += `
            <div class="cart-item" data-product-id="${item.productId}">
                <div class="no-image">📦</div>
                <div class="item-details">
                    <div class="item-name">${item.productName}</div>
                    <div class="item-price">Rs. ${formatPrice(item.productPrice)}</div>
                </div>
                <div class="item-controls">
                    <div class="quantity-controls">
                        <button class="qty-btn" onclick="updateQuantity('${item.productId}', ${item.quantity - 1})" 
                                ${item.quantity <= 1 ? 'disabled' : ''}>-</button>
                        <div class="quantity-display">${item.quantity}</div>
                        <button class="qty-btn" onclick="updateQuantity('${item.productId}', ${item.quantity + 1})">+</button>
                    </div>
                    <button class="remove-btn" onclick="removeFromCart('${item.productId}')">Remove</button>
                </div>
            </div>
        `;
    });

    container.innerHTML = itemsHTML;

    // Update summary
    document.getElementById('totalItems').textContent = cartItems.length;
    document.getElementById('totalAmount').textContent = `Rs. ${formatPrice(total)}`;
    document.getElementById('modalTotal').textContent = formatPrice(total);

    // Enable checkout button
    document.getElementById('checkoutBtn').disabled = false;
}

// Update item quantity
async function updateQuantity(productId, newQuantity) {
    if (newQuantity < 1) return;

    try {
        const response = await fetch('/cart/update', {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                productId: productId,
                quantity: newQuantity
            })
        });

        const result = await response.json();

        if (result.success) {
            loadCartItems(); // Reload cart
            Auth.updateCartCount(); // Update navigation count
        } else {
            showMessage(result.message, 'error');
        }
    } catch (error) {
        console.error('Error updating quantity:', error);
        showMessage('Failed to update quantity', 'error');
    }
}

// Remove item from cart
async function removeFromCart(productId) {
    if (!confirm('Are you sure you want to remove this item from your cart?')) {
        return;
    }

    try {
        const response = await fetch(`/cart/remove/${productId}`, {
            method: 'DELETE'
        });

        const result = await response.json();

        if (result.success) {
            showMessage('Item removed from cart', 'success');
            loadCartItems(); // Reload cart
            Auth.updateCartCount(); // Update navigation count
        } else {
            showMessage(result.message, 'error');
        }
    } catch (error) {
        console.error('Error removing item:', error);
        showMessage('Failed to remove item', 'error');
    }
}

// Clear entire cart
async function clearCart() {
    if (!confirm('Are you sure you want to clear your entire cart?')) {
        return;
    }

    try {
        const response = await fetch('/cart/clear', {
            method: 'DELETE'
        });

        const result = await response.json();

        if (result.success) {
            showMessage('Cart cleared successfully', 'success');
            loadCartItems(); // Reload cart
            Auth.updateCartCount(); // Update navigation count
        } else {
            showMessage(result.message, 'error');
        }
    } catch (error) {
        console.error('Error clearing cart:', error);
        showMessage('Failed to clear cart', 'error');
    }
}

// Proceed to checkout
async function proceedToCheckout() {
    // Get current user info to pre-fill form
    try {
        const user = await Auth.getCurrentUser();
        if (user) {
            document.getElementById('customerName').value = user.fullName || '';
            document.getElementById('customerEmail').value = user.email || '';
        }
    } catch (error) {
        console.log('Could not pre-fill user data');
    }

    // Show checkout modal
    document.getElementById('checkoutModal').classList.add('show');
}

// Close checkout modal
function closeCheckoutModal() {
    document.getElementById('checkoutModal').classList.remove('show');
}

// Place order
async function placeOrder() {
    const customerName = document.getElementById('customerName').value.trim();
    const customerEmail = document.getElementById('customerEmail').value.trim();
    const shippingAddress = document.getElementById('shippingAddress').value.trim();

    if (!customerName || !customerEmail || !shippingAddress) {
        showMessage('Please fill in all required fields', 'error');
        return;
    }

    try {
        // Show loading state
        const submitBtn = document.querySelector('.place-order-btn');
        const originalText = submitBtn.textContent;
        submitBtn.textContent = 'Placing Order...';
        submitBtn.disabled = true;

        const response = await fetch('/orders/create', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                customerName: customerName,
                customerEmail: customerEmail,
                shippingAddress: shippingAddress
            })
        });

        const result = await response.json();

        // Reset button
        submitBtn.textContent = originalText;
        submitBtn.disabled = false;

        if (result.success) {
            showMessage('Order placed successfully!', 'success');
            closeCheckoutModal();

            // Redirect to orders page after a delay
            setTimeout(() => {
                window.location.href = 'orders.html';
            }, 2000);

        } else {
            showMessage(result.message, 'error');
        }

    } catch (error) {
        console.error('Error placing order:', error);
        showMessage('Failed to place order', 'error');

        // Reset button
        const submitBtn = document.querySelector('.place-order-btn');
        submitBtn.textContent = 'Place Order';
        submitBtn.disabled = false;
    }
}

// Utility functions
function formatPrice(price) {
    return parseFloat(price).toLocaleString('en-LK', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
    });
}

function showMessage(message, type) {
    const messageContainer = document.getElementById('messageContainer');
    const messageContent = document.getElementById('messageContent');

    messageContent.textContent = message;
    messageContent.className = `message-content ${type}`;
    messageContainer.style.display = 'block';

    setTimeout(() => {
        messageContainer.style.display = 'none';
    }, 5000);
}

// Close modal when clicking outside
document.getElementById('checkoutModal').addEventListener('click', function(e) {
    if (e.target === this) {
        closeCheckoutModal();
    }
});