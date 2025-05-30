package com.example.studentattendancemanagementapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddClassActivity extends AppCompatActivity {

    EditText nameClassInput, limitAbsentInput, createCodeInput;
    Button createBtn;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_class);

        // Initialize views
        nameClassInput = findViewById(R.id.nameClass);
        limitAbsentInput = findViewById(R.id.limitAbsent);
        createCodeInput = findViewById(R.id.createCode);
        createBtn = findViewById(R.id.btnCreate);

        db = FirebaseFirestore.getInstance();

        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nameClass = nameClassInput.getText().toString().trim();
                String limitAbsent = limitAbsentInput.getText().toString().trim();
                String createCode = createCodeInput.getText().toString().trim();

                if (nameClass.isEmpty() || limitAbsent.isEmpty() || createCode.isEmpty()) {
                    Toast.makeText(AddClassActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Check if class code already exists
                db.collection("classes").document(createCode).get().addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Toast.makeText(AddClassActivity.this, "Code already exists. Please use a different code.", Toast.LENGTH_LONG).show();
                    } else {
                        // Save class data to Firestore
                        Map<String, Object> classData = new HashMap<>();
                        classData.put("name", nameClass);
                        classData.put("limitAbsent", Integer.parseInt(limitAbsent));
                        classData.put("createdBy", FirebaseAuth.getInstance().getUid());
                        classData.put("password", createCode); // 🔑 Add password field

                        db.collection("classes").document(createCode).set(classData)
                                .addOnSuccessListener(unused -> {
                                    Toast.makeText(AddClassActivity.this, "Class created successfully!", Toast.LENGTH_SHORT).show();
                                    finish();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(AddClassActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                    }
                }).addOnFailureListener(e -> {
                    Toast.makeText(AddClassActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        });
    }
}