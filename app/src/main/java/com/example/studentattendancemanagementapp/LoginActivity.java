package com.example.studentattendancemanagementapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText edtEmail, edtPassword; // Corrected to TextInputEditText
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtEmail = findViewById(R.id.etUsername); // Corrected ID reference
        edtPassword = findViewById(R.id.etPassword); // Corrected ID reference

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        findViewById(R.id.btnLoginForm).setOnClickListener(v -> loginUser()); // Corrected button ID reference
    }

    private void loginUser() {
        String email = edtEmail.getText().toString();
        String password = edtPassword.getText().toString();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(LoginActivity.this, "Please fill in both fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Sign in user with Firebase Auth
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Retrieve the current user
                        FirebaseUser user = mAuth.getCurrentUser();

                        // Check if the user is not null
                        if (user != null) {
                            // Get the user data from Firestore
                            DocumentReference docRef = db.collection("users").document(user.getUid());
                            docRef.get().addOnCompleteListener(docTask -> {
                                if (docTask.isSuccessful()) {
                                    // Get the user data (name and role)
                                    String name = docTask.getResult().getString("fullName"); // Use "fullName" if that’s your Firestore field
                                    String role = docTask.getResult().getString("role");

                                    // Save to SharedPreferences
                                    getSharedPreferences("UserPrefs", MODE_PRIVATE)
                                            .edit()
                                            .putString("name", name)
                                            .putString("role", role)
                                            .apply();

                                    // Pass the name and role to the HomePage activity
                                    Intent intent = new Intent(LoginActivity.this, HomePage.class);
                                    intent.putExtra("name", name);
                                    intent.putExtra("role", role);
                                    startActivity(intent);
                                    finish(); // Close login activity
                                } else {
                                    Toast.makeText(LoginActivity.this, "Failed to load user data.", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "Login failed. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}

