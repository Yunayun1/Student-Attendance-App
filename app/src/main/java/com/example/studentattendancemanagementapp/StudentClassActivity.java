package com.example.studentattendancemanagementapp;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import Adapter.StudentAdapter;
import Model.AttenStudent;

public class StudentClassActivity extends AppCompatActivity {

    private List<AttenStudent> students;
    private StudentAdapter adapter;
    private FirebaseFirestore db;
    private String classId;
    private String className;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_class);

        students = new ArrayList<>();
        db = FirebaseFirestore.getInstance();

        classId = getIntent().getStringExtra("CLASS_ID");
        className = getIntent().getStringExtra("CLASS_NAME");

        TextView classTitle = findViewById(R.id.textClassTitle);
        classTitle.setText("Class: " + className);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewStudents);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new StudentAdapter(this, students, false);  // not editable
        recyclerView.setAdapter(adapter);

        loadStudents();
    }

    private void loadStudents() {
        db.collection("classes")
                .document(classId)
                .collection("student")
                .get()
                .addOnSuccessListener(query -> {
                    students.clear();
                    for (QueryDocumentSnapshot doc : query) {
                        AttenStudent student = doc.toObject(AttenStudent.class);
                        students.add(student);
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to load students", Toast.LENGTH_SHORT).show();
                });
    }
}
