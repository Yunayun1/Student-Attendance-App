package com.example.studentattendancemanagementapp;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import Model.AttenStudent;
import Adapter.StudentAdapter;

import java.util.ArrayList;
import java.util.List;

public class ClassAttendanceActivity extends AppCompatActivity implements StudentAdapter.OnItemDoubleClickListener {

    private List<AttenStudent> students;
    private StudentAdapter adapter;
    private TextView presentCount;
    private TextView absentCount;
    private Button saveButton;
    private boolean isEditable = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_attendance);

        String className = getIntent().getStringExtra("CLASS_NAME");
        TextView classTitle = findViewById(R.id.classTitle);
        classTitle.setText("Class: " + className);

        presentCount = findViewById(R.id.presentCount);
        absentCount = findViewById(R.id.absentCount);
        saveButton = findViewById(R.id.saveButton);

        presentCount.setText("Present: 0");
        absentCount.setText("Absent: 0");

        students = new ArrayList<>();
        RecyclerView studentRecycler = findViewById(R.id.studentListRecycler);
        studentRecycler.setLayoutManager(new LinearLayoutManager(this));

        adapter = new StudentAdapter(this, students, isEditable);
        adapter.setOnItemDoubleClickListener(this);
        studentRecycler.setAdapter(adapter);

        // Swipe to delete
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView,
                                  RecyclerView.ViewHolder viewHolder,
                                  RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                new AlertDialog.Builder(ClassAttendanceActivity.this)
                        .setTitle("Delete Student")
                        .setMessage("Are you sure you want to delete this student?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            students.remove(position);

                            // Re-number students
                            for (int i = 0; i < students.size(); i++) {
                                students.get(i).setId(i + 1);
                            }

                            adapter.notifyDataSetChanged(); // Notify full data changed because IDs changed
                            updateCounts();
                        })
                        .setNegativeButton("No", (dialog, which) -> adapter.notifyItemChanged(position))
                        .setCancelable(false)
                        .show();
            }

        });
        itemTouchHelper.attachToRecyclerView(studentRecycler);

        // Menu popup
        ImageView menuButton = findViewById(R.id.menuButton);
        menuButton.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(ClassAttendanceActivity.this, menuButton);
            popup.getMenuInflater().inflate(R.menu.attendance_menu, popup.getMenu());
            popup.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.menu_new) {
                    showAddStudentDialog();
                    return true;
                } else if (item.getItemId() == R.id.menu_edit) {
                    isEditable = true;
                    adapter.setEditable(true);
                    saveButton.setVisibility(View.VISIBLE);
                    return true;
                }
                return false;
            });
            popup.show();
        });

        // Save button behavior
        saveButton.setOnClickListener(v -> {
            isEditable = false;
            adapter.setEditable(false);
            for (AttenStudent student : students) {
                // You can do save logic here if needed
            }
            saveButton.setVisibility(View.GONE);
            updateCounts();
        });

        updateCounts();
    }

    private void updateCounts() {
        int present = 0;
        for (AttenStudent student : students) {
            if (student.isPresent()) present++;
        }
        presentCount.setText("Present: " + present);
        absentCount.setText("Absent: " + (students.size() - present));
    }

    private void showAddStudentDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add New Student");

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_student_edit, null);
        EditText nameInput = dialogView.findViewById(R.id.editTextName);
        Spinner genderSpinner = dialogView.findViewById(R.id.spinnerGender);

        builder.setView(dialogView);

        builder.setPositiveButton("Add", (dialog, which) -> {
            String name = nameInput.getText().toString().trim();
            String gender = genderSpinner.getSelectedItem().toString();

            if (name.isEmpty()) {
                Toast.makeText(this, "Please enter a name", Toast.LENGTH_SHORT).show();
                return;
            }

            int id = students.size() + 1;
            AttenStudent newStudent = new AttenStudent(id, name, gender, true); // Present = true by default
            students.add(newStudent);
            adapter.notifyItemInserted(students.size() - 1);
            updateCounts();
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builder.show();
    }

    @Override
    public void onItemDoubleClicked(int position) {
        AttenStudent student = students.get(position);
        student.setPresent(!student.isPresent());  // toggle present/absent
        adapter.notifyItemChanged(position);
        updateCounts();
    }
}