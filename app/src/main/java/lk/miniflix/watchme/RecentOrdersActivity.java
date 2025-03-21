package lk.miniflix.watchme;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import lk.miniflix.watchme.adapters.RecentOrdersAdapter;
import lk.miniflix.watchme.module.RecentOrderModel;

public class RecentOrdersActivity extends AppCompatActivity {

    private FirebaseFirestore firestore;
    private RecyclerView recyclerViewRecentOrders;
    private RecentOrdersAdapter recentOrdersAdapter;
    private List<RecentOrderModel> recentOrdersList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_orders);

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance();

        // Initialize RecyclerView
        recyclerViewRecentOrders = findViewById(R.id.recyclerViewRecentOrders);
        recyclerViewRecentOrders.setLayoutManager(new LinearLayoutManager(this));

        // Initialize Adapter
        recentOrdersAdapter = new RecentOrdersAdapter(recentOrdersList, this);
        recyclerViewRecentOrders.setAdapter(recentOrdersAdapter);

        // Load recent orders
        loadRecentOrders();

        // Set click listener
        recentOrdersAdapter.setOnItemClickListener((position, order) -> {
            // Navigate to OrderDetailsActivity with order details
            Intent intent = new Intent(RecentOrdersActivity.this, OrderDetailsActivity.class);
            intent.putExtra("orderId", order.getOrderId());
            intent.putExtra("customerName", order.getCustomerName());
            intent.putExtra("totalAmount", order.getTotalAmount());
            intent.putExtra("date", order.getDate());
            startActivity(intent);
        });

        // Set long-click listener
        recentOrdersAdapter.setOnItemLongClickListener((position, order) -> {
            deleteOrder(order.getOrderId());
            return true;
        });
    }

    private void loadRecentOrders() {
        firestore.collection("orders")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        recentOrdersList.clear();
                        for (QueryDocumentSnapshot snapshot : task.getResult()) {
                            RecentOrderModel order = snapshot.toObject(RecentOrderModel.class);
                            recentOrdersList.add(order);
                        }
                        recentOrdersAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(this, "Failed to load orders", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void deleteOrder(String orderId) {
        firestore.collection("orders")
                .document(orderId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Order deleted successfully", Toast.LENGTH_SHORT).show();
                    loadRecentOrders(); // Refresh the list
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to delete order", Toast.LENGTH_SHORT).show();
                });
    }
}