package com.example.studentattendancemanagementapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import Adapter.ClassListAdapter;
import Model.ClassItem;

public class StudentsActivity extends AppCompatActivity implements ClassListAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private ClassListAdapter adapter;
    private List<ClassItem> classList;
    private FirebaseFirestore db;
    private String userId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students);

        db = FirebaseFirestore.getInstance();
        userId = FirebaseAuth.getInstance().getUid();

        classList = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerViewStudentClasses);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ClassListAdapter(this, classList, this);
        recyclerView.setAdapter(adapter);

        loadJoinedClasses();
    }

    private void loadJoinedClasses() {
        db.collection("classes")
                .whereArrayContains("joinedStudents", userId)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    classList.clear();
                    for (QueryDocumentSnapshot doc : querySnapshot) {
                        String id = doc.getId();
                        String name = doc.getString("className");
                        classList.add(new ClassItem(id, name));
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed to load classes", Toast.LENGTH_SHORT).show());
    }

    @Override
    public void onItemClick(int position) {
        ClassItem classItem = classList.get(position);
        Intent intent = new Intent(StudentsActivity.this, StudentClassActivity.class);
        intent.putExtra("CLASS_ID", classItem.getId());
        intent.putExtra("CLASS_NAME", classItem.getName());
        startActivity(intent);
    }
}
