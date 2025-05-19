package com.example.studentattendancemanagementapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;
import android.widget.Toast;


import Adapter.ClassAdapter;
import Model.ClassModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AttendanceActivity extends AppCompatActivity {

    Button plusButton;
    ImageButton backButton;
    RecyclerView classRecyclerView;
    ClassAdapter classAdapter;
    List<ClassModel> classList = new ArrayList<>();
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        plusButton = findViewById(R.id.plusButton);
        backButton = findViewById(R.id.backButton);
        classRecyclerView = findViewById(R.id.classRecyclerView);

        prefs = getSharedPreferences("ClassPrefs", MODE_PRIVATE);

        loadClasses();

        classAdapter = new ClassAdapter(classList);
        classRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        classRecyclerView.setAdapter(classAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false; // we don’t need drag & drop
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                ClassModel classModel = classList.get(position);

                new androidx.appcompat.app.AlertDialog.Builder(AttendanceActivity.this)
                        .setTitle("Delete Class")
                        .setMessage("Are you sure you want to delete \"" + classModel.getClassName() + "\"?")
                        .setPositiveButton("Delete", (dialog, which) -> {
                            // Delete from SharedPreferences
                            Map<String, ?> allClasses = prefs.getAll();
                            for (Map.Entry<String, ?> entry : allClasses.entrySet()) {
                                String value = entry.getValue().toString();
                                if (value.startsWith(classModel.getClassName() + ";")) {
                                    prefs.edit().remove(entry.getKey()).apply();
                                    break;
                                }
                            }

                            // Remove from list and update adapter
                            classList.remove(position);
                            classAdapter.notifyItemRemoved(position);

                            Toast.makeText(AttendanceActivity.this, "Class deleted", Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton("Cancel", (dialog, which) -> {
                            classAdapter.notifyItemChanged(position); // restore item if canceled
                            dialog.dismiss();
                        })
                        .setCancelable(false)
                        .show();
            }

        });
        itemTouchHelper.attachToRecyclerView(classRecyclerView);


        plusButton.setOnClickListener(v -> {
            Intent intent = new Intent(AttendanceActivity.this, AddClassActivity.class);
            startActivity(intent);
        });
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
        loadClasses(); // reload when coming back from AddClassActivity
        classAdapter.notifyDataSetChanged(); // update UI
    }

    private void loadClasses() {
        classList.clear();
        Map<String, ?> allClasses = prefs.getAll();
        for (Map.Entry<String, ?> entry : allClasses.entrySet()) {
            String value = entry.getValue().toString(); // This is in format: className;limitAbsent
            String[] parts = value.split(";");
            if (parts.length >= 1) {
                String className = parts[0];
                classList.add(new ClassModel(className));
            }
        }
    }

}