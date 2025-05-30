package com.example.studentattendancemanagementapp;

import android.content.Intent;
import android.os.Bundle;
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

        createBtn.setOnClickListener(view -> {
            String nameClass = nameClassInput.getText().toString().trim();
            String limitAbsent = limitAbsentInput.getText().toString().trim();
            String createCode = createCodeInput.getText().toString().trim();

            if (nameClass.isEmpty() || limitAbsent.isEmpty() || createCode.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Check if class code already exists
            db.collection("classes").document(createCode).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            Toast.makeText(this, "Code already exists. Please use a different code.", Toast.LENGTH_LONG).show();
                        } else {
                            // Create class data
                            Map<String, Object> classData = new HashMap<>();
                            classData.put("className", nameClass);
                            classData.put("limitAbsent", Integer.parseInt(limitAbsent));
                            classData.put("createdBy", FirebaseAuth.getInstance().getUid());
                            classData.put("password", createCode); // Use code as password

                            db.collection("classes").document(createCode).set(classData)
                                    .addOnSuccessListener(unused -> {
                                        Toast.makeText(this, "Class created successfully!", Toast.LENGTH_SHORT).show();

                                        // Navigate to ClassAttendanceActivity
                                        Intent intent = new Intent(this, ClassAttendanceActivity.class);
                                        intent.putExtra("CLASS_CODE", createCode);
                                        intent.putExtra("CLASS_NAME", nameClass);
                                        startActivity(intent);

                                        finish(); // Close this activity
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(this, "Failed to create class: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });
    }
}
