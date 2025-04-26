package com.example.studentattendancemanagementapp;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.*;

public class EditProfileActivity extends AppCompatActivity {
    private TextInputEditText etName, etUsername, etGender, etPhone, etEmail;
    private MaterialButton btnSave;
    private DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // Toolbar + back arrow
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        if (ab != null) ab.setDisplayHomeAsUpEnabled(true);

        // Bind views
        etName     = findViewById(R.id.etName);
        etUsername = findViewById(R.id.etUsername);
        etGender   = findViewById(R.id.etGender);
        etPhone    = findViewById(R.id.etPhone);
        etEmail    = findViewById(R.id.etEmail);
        btnSave    = findViewById(R.id.btnSave);

        // Get UID from intent
        String uid = getIntent().getStringExtra("uid");
        if (uid == null || uid.isEmpty()) {
            Toast.makeText(this, "UID is missing", Toast.LENGTH_SHORT).show();
            finish(); // Close if no UID
            return;
        }

        Log.d("EditProfileActivity", "UID received: " + uid);

        // Get reference to user in Firebase
        userRef = FirebaseDatabase.getInstance().getReference("users").child(uid);

        // Load user data
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override public void onDataChange(@NonNull DataSnapshot snap) {
                if (snap.exists()) {
                    etName.setText(snap.child("fullName").getValue(String.class));
                    etUsername.setText(snap.child("username").getValue(String.class));
                    etGender.setText(snap.child("gender").getValue(String.class));
                    etPhone.setText(snap.child("phoneNumber").getValue(String.class));
                    etEmail.setText(snap.child("email").getValue(String.class));
                }
            }

            @Override public void onCancelled(@NonNull DatabaseError err) {
                Toast.makeText(EditProfileActivity.this,
                        "Load failed: " + err.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Save button
        btnSave.setOnClickListener(v -> {
            String name  = etName.getText().toString().trim();
            String usern = etUsername.getText().toString().trim();
            String gen   = etGender.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();
            String email = etEmail.getText().toString().trim();

            if (name.isEmpty() || usern.isEmpty() || gen.isEmpty() || phone.isEmpty() || email.isEmpty()) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
                return;
            }

            // Save updated values to Firebase
            userRef.child("fullName").setValue(name);
            userRef.child("username").setValue(usern);
            userRef.child("gender").setValue(gen);
            userRef.child("phoneNumber").setValue(phone);
            userRef.child("email").setValue(email)
                    .addOnSuccessListener(a -> {
                        Toast.makeText(this, "Profile saved!", Toast.LENGTH_SHORT).show();
                        finish(); // go back to profile screen
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(this, "Save failed: " + e.getMessage(),
                                    Toast.LENGTH_SHORT).show()
                    );
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Back arrow
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
