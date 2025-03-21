package lk.miniflix.watchme;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;

import lk.miniflix.watchme.module.RecentOrderModel;

public class OrderDetailsActivity extends AppCompatActivity {

    private FirebaseFirestore firestore;
    private TextView textOrderId, textCustomerName, textTotalAmount, textOrderDate, textOrderItems, textOrderStatus, textShippingAddress, textPaymentMethod;
    private String orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance();

        // Initialize Views
        textOrderId = findViewById(R.id.textOrderId);
        textCustomerName = findViewById(R.id.textCustomerName);
        textTotalAmount = findViewById(R.id.textTotalAmount);
        textOrderDate = findViewById(R.id.textOrderDate);
        textOrderItems = findViewById(R.id.textOrderItems);
        textOrderStatus = findViewById(R.id.textOrderStatus);
        textShippingAddress = findViewById(R.id.textShippingAddress);
        textPaymentMethod = findViewById(R.id.textPaymentMethod);

        // Get order ID from intent
        orderId = getIntent().getStringExtra("orderId");

        // Load order details
        loadOrderDetails();
    }

    private void loadOrderDetails() {
        firestore.collection("orders")
                .document(orderId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            RecentOrderModel order = document.toObject(RecentOrderModel.class);
                            if (order != null) {
                                // Populate views with order data
                                textOrderId.setText("Order ID: " + order.getOrderId());
                                textCustomerName.setText("Customer: " + order.getCustomerName());
                                textTotalAmount.setText("Amount: $" + order.getTotalAmount());
                                textOrderDate.setText("Date: " + order.getDate());
                                textOrderItems.setText("Items: " + order.getItems());
                                textOrderStatus.setText("Status: " + order.getStatus());
                                textShippingAddress.setText("Shipping Address: " + order.getShippingAddress());
                                textPaymentMethod.setText("Payment Method: " + order.getPaymentMethod());
                            }
                        } else {
                            Toast.makeText(this, "Order not found", Toast.LENGTH_SHORT).show();
                            finish(); // Close the activity if the order doesn't exist
                        }
                    } else {
                        Toast.makeText(this, "Failed to load order details", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}