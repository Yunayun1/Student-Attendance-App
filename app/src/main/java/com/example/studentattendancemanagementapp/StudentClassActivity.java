package com.example.studentattendancemanagementapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import Adapter.ClassAdapter;
import Model.ClassModel;

public class StudentClassActivity extends AppCompatActivity {

    ImageButton backButton;
    RecyclerView classRecyclerView;
    ClassAdapter classAdapter;
    List<ClassModel> classList;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_class);

        db = FirebaseFirestore.getInstance();

        backButton = findViewById(R.id.backButton);
        classRecyclerView = findViewById(R.id.classRecyclerView);
        classList = new ArrayList<>();

        classRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        classAdapter = new ClassAdapter(this, classList);
        classRecyclerView.setAdapter(classAdapter);

        loadJoinedClasses();

        backButton.setOnClickListener(v -> finish());
    }

    private void loadJoinedClasses() {
        classList.clear();

        SharedPreferences prefs = getSharedPreferences("ClassPrefs", MODE_PRIVATE);
        // Get all joined class codes
        for (String code : prefs.getAll().keySet()) {
            db.collection("classes")
                    .whereEqualTo("className", code)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            String className = document.getString("className");
                            if (className != null) {
                                classList.add(new ClassModel(className));
                            }
                        }
                        classAdapter.notifyDataSetChanged();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Failed to load class: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
    }
}