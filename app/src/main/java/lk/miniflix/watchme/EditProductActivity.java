package lk.miniflix.watchme;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import lk.miniflix.watchme.module.ProductModel;

public class EditProductActivity extends AppCompatActivity {

    private FirebaseFirestore firestore;
    private TextInputEditText editTextProductName, editTextProductCategory, editTextProductPrice;
    private Button buttonSave, buttonDelete;
    private String productId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance();

        // Initialize Views
        editTextProductName = findViewById(R.id.editTextProductName);
        editTextProductCategory = findViewById(R.id.editTextProductCategory);
        editTextProductPrice = findViewById(R.id.editTextProductPrice);
        buttonSave = findViewById(R.id.buttonSave);
        buttonDelete = findViewById(R.id.buttonDelete);

        // Get product details from intent
        Intent intent = getIntent();
        productId = intent.getStringExtra("name");
        String name = intent.getStringExtra("name");
        String category = intent.getStringExtra("category");
        double price = intent.getDoubleExtra("price", 0.0);

        // Populate fields with product data
        editTextProductName.setText(name);
        editTextProductCategory.setText(category);
        editTextProductPrice.setText(String.valueOf(price));

        // Save Changes Button
        buttonSave.setOnClickListener(v -> saveChanges());

        // Delete Product Button
        buttonDelete.setOnClickListener(v -> deleteProduct());
    }

    private void saveChanges() {
        String name = editTextProductName.getText().toString().trim();
        String category = editTextProductCategory.getText().toString().trim();
        String priceString = editTextProductPrice.getText().toString().trim();

        if (name.isEmpty() || category.isEmpty() || priceString.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double price = Double.parseDouble(priceString);

        // Create a map of updated product data
        Map<String, Object> product = new HashMap<>();
        product.put("name", name);
        product.put("category", category);
        product.put("price", price);

        // Update product in Firestore
        firestore.collection("products")
                .document(productId)
                .update(product)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Product updated successfully", Toast.LENGTH_SHORT).show();
                    finish(); // Close the activity
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to update product", Toast.LENGTH_SHORT).show();
                });
    }

    private void deleteProduct() {
        firestore.collection("products")
                .document(productId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Product deleted successfully", Toast.LENGTH_SHORT).show();
                    finish(); // Close the activity
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to delete product", Toast.LENGTH_SHORT).show();
                });
    }
}