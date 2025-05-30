package com.example.studentattendancemanagementapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class JoinClassActivity extends AppCompatActivity {

    EditText joinCodeInput;
    Button joinBtn;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_class);

        joinCodeInput = findViewById(R.id.joinCode);
        joinBtn = findViewById(R.id.joinBtn);

        db = FirebaseFirestore.getInstance();

        joinBtn.setOnClickListener(v -> {
            String code = joinCodeInput.getText().toString().trim();

            if (code.isEmpty()) {
                Toast.makeText(this, "Please enter a class code", Toast.LENGTH_SHORT).show();
                return;
            }

            // Check if class exists in Firestore
            DocumentReference classRef = db.collection("classes").document(code);
            classRef.get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    String className = documentSnapshot.getString("className");

                    Toast.makeText(this, "Joined class successfully!", Toast.LENGTH_SHORT).show();

                    // Go to ClassAttendanceActivity
                    Intent intent = new Intent(JoinClassActivity.this, StudentsActivity.class);
                    intent.putExtra("CLASS_CODE", code);
                    intent.putExtra("CLASS_NAME", className);
                    startActivity(intent);

                } else {
                    Toast.makeText(this, "Class code not found!", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(e ->
                    Toast.makeText(this, "Error checking class: " + e.getMessage(), Toast.LENGTH_SHORT).show()
            );
        });
    }
}
