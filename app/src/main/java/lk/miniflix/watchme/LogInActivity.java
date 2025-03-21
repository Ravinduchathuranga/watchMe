package lk.miniflix.watchme;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LogInActivity extends AppCompatActivity {
    private EditText emailText, passwordText;
    private Button button;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_log_in);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        firebaseAuth = FirebaseAuth.getInstance();
        emailText = findViewById(R.id.editTextEmail);
        passwordText = findViewById(R.id.editTextPassword);
        button = findViewById(R.id.signInButton);
        button.setOnClickListener(v -> userAuth());
    }

    private void userAuth() {
        String userEmail = emailText.getText().toString();
        String userPassword = passwordText.getText().toString();
        if (userEmail.isEmpty()) {
            Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show();
        } else if (userPassword.isEmpty()) {
            Toast.makeText(this, "Please enter your password", Toast.LENGTH_SHORT).show();
        } else {
            firebaseAuth.signInWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(this, task -> {
                if (task.isSuccessful()) {
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    if (user.getEmail().equals("adminuser@watchme.com")) {
                        Intent intent = new Intent(LogInActivity.this, DashboardActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Error : " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}