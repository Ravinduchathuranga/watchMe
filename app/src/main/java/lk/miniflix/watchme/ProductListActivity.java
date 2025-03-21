package lk.miniflix.watchme;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import lk.miniflix.watchme.adapters.ProductAdapter;
import lk.miniflix.watchme.module.ProductModel;

public class ProductListActivity extends AppCompatActivity {

    private FirebaseFirestore firestore;
    private RecyclerView recyclerViewProducts;
    private ProductAdapter productAdapter;
    private List<ProductModel> productList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance();

        // Initialize RecyclerView
        recyclerViewProducts = findViewById(R.id.recyclerViewProducts);
        recyclerViewProducts.setLayoutManager(new LinearLayoutManager(this));

        // Initialize Adapter
        productAdapter = new ProductAdapter(productList, this);
        recyclerViewProducts.setAdapter(productAdapter);

        // Load product list
        loadProductList();

        // Floating Action Button (FAB) to Add Product
        FloatingActionButton fabAddProduct = findViewById(R.id.fabAddProduct);
        fabAddProduct.setOnClickListener(v -> {
            startActivity(new Intent(ProductListActivity.this, AddProductActivity.class));
        });

        // Set item click listener for RecyclerView
        productAdapter.setOnItemClickListener((position, product) -> {
            // Navigate to EditProductActivity with product details
            Intent intent = new Intent(ProductListActivity.this, EditProductActivity.class);
            intent.putExtra("productId", product.getProductId());
            intent.putExtra("name", product.getName());
            intent.putExtra("category", product.getCategory());
            intent.putExtra("price", product.getPrice());
            intent.putExtra("imageUrl", product.getImageUrl());
            startActivity(intent);
        });

        // Set item long click listener for RecyclerView (delete product)
        productAdapter.setOnItemLongClickListener((position, product) -> {
            deleteProduct(product.getProductId());
            return true;
        });
    }

    private void loadProductList() {
        firestore.collection("products")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        productList.clear();
                        for (QueryDocumentSnapshot snapshot : task.getResult()) {
                            ProductModel product = snapshot.toObject(ProductModel.class);
                            productList.add(product);
                        }
                        productAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(this, "Failed to load products", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void deleteProduct(String productId) {
        firestore.collection("products")
                .document(productId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Product deleted", Toast.LENGTH_SHORT).show();
                    loadProductList(); // Refresh the list
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to delete product", Toast.LENGTH_SHORT).show();
                });
    }
}