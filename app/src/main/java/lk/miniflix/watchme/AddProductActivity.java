package lk.miniflix.watchme;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.cloudinary.android.MediaManager;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import lk.miniflix.watchme.module.CloudinaryHelper;
import lk.miniflix.watchme.module.InputValidations;


public class AddProductActivity extends AppCompatActivity {

    private EditText productNameText, productDescText, productPriceText, brandText, featuresText;
    private Spinner categorySpinner;
    private ImageView imageView;
    private Button addImageBtn;

    private Uri imageUri;
    private FirebaseFirestore firestore;
    private ActivityResultLauncher<PickVisualMediaRequest> pickMedia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance();

        // Initialize UI elements
        imageView = findViewById(R.id.imageViewPreview);
        addImageBtn = findViewById(R.id.addImageButton);
        productNameText = findViewById(R.id.editTextProductName);
        productDescText = findViewById(R.id.editTextProductDescription);
        productPriceText = findViewById(R.id.editTextPrice);
        brandText = findViewById(R.id.editTextBrand);
        featuresText = findViewById(R.id.editTextFeatures);
        categorySpinner = findViewById(R.id.spinnerCategory);

        // Set up category spinner
        setupCategorySpinner();

        // Initialize Cloudinary
        Map config = new HashMap();
        config.put("cloud_name", "ddqbsnrm6");
        config.put("api_key", "883161424141396");
        config.put("api_secret", "MxrVjJxZyXRM85qV8xzFqQnAbp4");
        config.put("upload_preset", "watchme");
        try {
            MediaManager.get();
        } catch (IllegalStateException e) {
            MediaManager.init(this, config);
        }

        // Set up image picker
        pickMedia = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
            if (uri != null) {
                imageUri = uri;
                imageView.setImageURI(uri);
            } else {
                Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show();
            }
        });

        addImageBtn.setOnClickListener(v -> openImagePicker());
        imageView.setOnClickListener(v -> openImagePicker());

        // Confirm button
        Button confirmButton = findViewById(R.id.buttonConfirm);
        confirmButton.setOnClickListener(v -> addProduct());

        // Cancel button
        Button cancelButton = findViewById(R.id.buttonCancel);
        cancelButton.setOnClickListener(v -> finish());
    }

    private void openImagePicker() {
        pickMedia.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                .build());
    }

    private void setupCategorySpinner() {
        String[] categories = getResources().getStringArray(R.array.watch_categories); // Update array in strings.xml
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, R.layout.spinner_text, categories);
        categorySpinner.setAdapter(categoryAdapter);
    }

    private void addProduct() {
        String productName = productNameText.getText().toString().trim();
        String productDesc = productDescText.getText().toString().trim();
        String productCategory = categorySpinner.getSelectedItem().toString();
        String productPrice = productPriceText.getText().toString().trim();
        String brand = brandText.getText().toString().trim();
        String features = featuresText.getText().toString().trim();

        if (!validateInputs(productName, productDesc, productCategory, productPrice, brand, features, imageUri)) {
            return;
        }

        saveProductToFirestore(productName, productDesc, productCategory, productPrice, brand, features);
    }

    private boolean validateInputs(String productName, String productDesc, String productCategory, String productPrice, String brand, String features, Uri uri) {
        if (uri == null) {
            showToast("Please add watch image");
            return false;
        } else if (productName.isEmpty()) {
            showToast("Please enter watch name");
            return false;
        } else if (brand.isEmpty()) {
            showToast("Please enter watch brand");
            return false;
        } else if (productDesc.isEmpty()) {
            showToast("Please enter watch description");
            return false;
        } else if (productCategory.equals("Select Category")) {
            showToast("Please select category");
            return false;
        } else if (productPrice.isEmpty()) {
            showToast("Please enter watch price");
            return false;
        } else if (!InputValidations.isDouble(productPrice)) {
            showToast("Please enter a valid price");
            return false;
        } else if (features.isEmpty()) {
            showToast("Please enter watch features");
            return false;
        }
        return true;
    }

    private void saveProductToFirestore(String productName, String productDesc, String productCategory, String productPrice, String brand, String features) {
        CloudinaryHelper.uploadImage(imageUri, null, new CloudinaryHelper.OnUploadCompleteListener() {
            @Override
            public void onUploadComplete(String url) {
                Map<String, Object> product = new HashMap<>();
                product.put("name", productName);
                product.put("description", productDesc);
                product.put("category", productCategory);
                product.put("price", Double.parseDouble(productPrice));
                product.put("brand", brand);
                product.put("features", features);
                product.put("imageUri", url);

                firestore.collection("watches")
                        .document(productName)
                        .set(product)
                        .addOnSuccessListener(documentReference -> {
                            showToast("Watch added successfully");
                            finish(); // Close the activity after adding the product
                        })
                        .addOnFailureListener(e -> {
                            showToast("Error: " + e.getMessage());
                        });
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}