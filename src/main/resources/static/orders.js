document.addEventListener('DOMContentLoaded', function() {
    // Check if user is logged in
    Auth.requireLogin().then(isLoggedIn => {
        if (isLoggedIn) {
            loadUserOrders();
        }
    });
});

// Load user orders
async function loadUserOrders() {
    try {
        const response = await fetch('/orders/my-orders');
        const result = await response.json();

        if (result.success) {
            displayOrders(result.orders);
        } else {
            showMessage(result.message, 'error');
            if (result.message.includes("log in")) {
                setTimeout(() => {
                    window.location.href = 'login.html';
                }, 2000);
            }
        }
    } catch (error) {
        console.error('Error loading orders:', error);
        showMessage('Failed to load orders', 'error');
    }
}

// Display orders
function displayOrders(orders) {
    const container = document.getElementById('ordersList');

    if (orders.length === 0) {
        container.innerHTML = `
            <div class="empty-orders">
                <div class="empty-orders-icon">📋</div>
                <h2>No orders yet</h2>
                <p>Start shopping to see your orders here!</p>
                <button onclick="window.location.href='lankabuy_browse_products.html'" class="continue-shopping-btn">
                    Start Shopping
                </button>
            </div>
        `;
        return;
    }

    // Build orders HTML
    let ordersHTML = '';
    orders.forEach(order => {
        ordersHTML += `
            <div class="order-item">
                <div class="order-header">
                    <div class="order-info">
                        <h3>Order #${order.orderId}</h3>
                        <div class="order-date">${formatDate(order.orderDate)}</div>
                    </div>
                    <div class="order-status status-${order.status.toLowerCase()}">${order.status}</div>
                </div>
                
                <div class="order-details">
                    <div class="detail-item">
                        <div class="detail-label">Customer</div>
                        <div class="detail-value">${order.customerName}</div>
                    </div>
                    <div class="detail-item">
                        <div class="detail-label">Email</div>
                        <div class="detail-value">${order.customerEmail}</div>
                    </div>
                    <div class="detail-item">
                        <div class="detail-label">Total Amount</div>
                        <div class="detail-value amount">Rs. ${formatPrice(order.totalAmount)}</div>
                    </div>
                </div>
                
                <div class="order-actions">
                    <button class="view-details-btn" onclick="viewOrderDetails(${order.orderId})">
                        View Details
                    </button>
                </div>
            </div>
        `;
    });

    container.innerHTML = ordersHTML;
}

// View order details
async function viewOrderDetails(orderId) {
    try {
        const response = await fetch(`/orders/${orderId}`);
        const result = await response.json();

        if (result.success) {
            displayOrderDetailsModal(result.order, result.orderItems);
        } else {
            showMessage(result.message, 'error');
        }
    } catch (error) {
        console.error('Error loading order details:', error);
        showMessage('Failed to load order details', 'error');
    }
}

// Display order details modal
function displayOrderDetailsModal(order, orderItems) {
    const modalContent = document.getElementById('orderDetailsContent');

    let itemsHTML = '';
    orderItems.forEach(item => {
        itemsHTML += `
            <div class="order-item-detail">
                <div class="item-info">
                    <div class="item-name">${item.productName}</div>
                    <div class="item-quantity">Quantity: ${item.quantity}</div>
                </div>
                <div class="item-price">Rs. ${formatPrice(item.price)}</div>
            </div>
        `;
    });

    modalContent.innerHTML = `
        <div class="modal-order-header">
            <h2>Order #${order.orderId}</h2>
            <div class="order-status status-${order.status.toLowerCase()}">${order.status}</div>
        </div>
        
        <div class="modal-order-summary">
            <div class="order-info-section">
                <h3>Order Information</h3>
                <div class="detail-item">
                    <div class="detail-label">Order Date</div>
                    <div class="detail-value">${formatDate(order.orderDate)}</div>
                </div>
                <div class="detail-item">
                    <div class="detail-label">Customer Name</div>
                    <div class="detail-value">${order.customerName}</div>
                </div>
                <div class="detail-item">
                    <div class="detail-label">Email</div>
                    <div class="detail-value">${order.customerEmail}</div>
                </div>
                <div class="detail-item">
                    <div class="detail-label">Shipping Address</div>
                    <div class="detail-value">${order.shippingAddress}</div>
                </div>
                <div class="detail-item">
                    <div class="detail-label">Total Amount</div>
                    <div class="detail-value amount">Rs. ${formatPrice(order.totalAmount)}</div>
                </div>
            </div>
            
            <div class="order-items-section">
                <h3>Order Items</h3>
                ${itemsHTML}
            </div>
        </div>
    `;

    document.getElementById('orderDetailsModal').classList.add('show');
}

// Close order details modal
function closeOrderDetailsModal() {
    document.getElementById('orderDetailsModal').classList.remove('show');
}

// Utility functions
function formatPrice(price) {
    return parseFloat(price).toLocaleString('en-LK', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
    });
}

function formatDate(dateString) {
    const date = new Date(dateString);
    return date.toLocaleDateString('en-US', {
        year: 'numeric',
        month: 'long',
        day: 'numeric',
        hour: '2-digit',
        minute: '2-digit'
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
document.getElementById('orderDetailsModal').addEventListener('click', function(e) {
    if (e.target === this) {
        closeOrderDetailsModal();
    }
});