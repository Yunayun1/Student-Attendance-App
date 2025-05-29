package com.example.studentattendancemanagementapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import Adapter.ClassAdapter;
import Model.ClassModel;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class AttendanceActivity extends AppCompatActivity {

    Button plusButton;
    ImageButton backButton;
    RecyclerView classRecyclerView;
    ClassAdapter classAdapter;
    List<ClassModel> classList;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize views
        plusButton = findViewById(R.id.plusButton);
        backButton = findViewById(R.id.backButton);
        classRecyclerView = findViewById(R.id.classRecyclerView);

        // Initialize class list and adapter
        classList = new ArrayList<>();
        classRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        classAdapter = new ClassAdapter(this, classList);
        classRecyclerView.setAdapter(classAdapter);

        // Load classes from Firestore
        loadClasses();

        // Swipe to delete class
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView,
                                  RecyclerView.ViewHolder viewHolder,
                                  RecyclerView.ViewHolder target) {
                return false; // no move support
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                ClassModel classModel = classList.get(position);
                String classNameToDelete = classModel.getClassName();

                new androidx.appcompat.app.AlertDialog.Builder(AttendanceActivity.this)
                        .setTitle("Delete Class")
                        .setMessage("Are you sure you want to delete \"" + classNameToDelete + "\"?")
                        .setPositiveButton("Delete", (dialog, which) -> {
                            // Delete class from Firestore
                            db.collection("classes")
                                    .whereEqualTo("className", classNameToDelete)
                                    .get()
                                    .addOnSuccessListener(queryDocumentSnapshots -> {
                                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                            db.collection("classes").document(document.getId())
                                                    .delete()
                                                    .addOnSuccessListener(unused -> {
                                                        Toast.makeText(AttendanceActivity.this, "Class deleted", Toast.LENGTH_SHORT).show();
                                                    })
                                                    .addOnFailureListener(e -> {
                                                        Toast.makeText(AttendanceActivity.this, "Failed to delete class: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                    });
                                        }
                                        classList.remove(position);
                                        classAdapter.notifyItemRemoved(position);
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(AttendanceActivity.this, "Error deleting class: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        classAdapter.notifyItemChanged(position); // restore item on failure
                                    });
                        })
                        .setNegativeButton("Cancel", (dialog, which) -> {
                            classAdapter.notifyItemChanged(position); // restore item on cancel
                            dialog.dismiss();
                        })
                        .setCancelable(false)
                        .show();
            }
        });
        itemTouchHelper.attachToRecyclerView(classRecyclerView);

        // Plus button click -> go to AddClassActivity
        plusButton.setOnClickListener(v -> {
            Intent intent = new Intent(AttendanceActivity.this, AddClassActivity.class);
            startActivity(intent);
        });

        // Back button click -> go to HomePage
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(AttendanceActivity.this, HomePage.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadClasses(); // reload classes when coming back from AddClassActivity
    }

    private void loadClasses() {
        classList.clear();

        db.collection("classes")
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
                    Toast.makeText(AttendanceActivity.this, "Failed to load classes: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
