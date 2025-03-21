package lk.miniflix.watchme.module;

public class RecentOrderModel {
    private String orderId;
    private String customerName;
    private double totalAmount;
    private String date;
    private String items; // List of items in the order
    private String status; // Order status (e.g., "Pending", "Delivered")
    private String shippingAddress; // Shipping address
    private String paymentMethod; // Payment method (e.g., "Credit Card", "Cash on Delivery")

    // Default constructor (required for Firestore)
    public RecentOrderModel() {}

    // Parameterized constructor
    public RecentOrderModel(String orderId, String customerName, double totalAmount, String date, String items, String status, String shippingAddress, String paymentMethod) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.totalAmount = totalAmount;
        this.date = date;
        this.items = items;
        this.status = status;
        this.shippingAddress = shippingAddress;
        this.paymentMethod = paymentMethod;
    }

    // Getters and Setters
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}