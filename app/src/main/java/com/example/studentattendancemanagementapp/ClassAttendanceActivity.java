package com.example.studentattendancemanagementapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.List;

import Adapter.StudentAdapter;
import Model.AttenStudent;

public class ClassAttendanceActivity extends AppCompatActivity implements StudentAdapter.OnItemDoubleClickListener {

    private List<AttenStudent> students;
    private StudentAdapter adapter;
    private TextView presentCount, absentCount;
    private Button saveButton;
    private boolean isEditable = false;
    private FirebaseFirestore db;
    private String className;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_attendance);

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            Toast.makeText(this, "Please sign in", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        db = FirebaseFirestore.getInstance();
        students = new ArrayList<>();

        className = getIntent().getStringExtra("CLASS_NAME");
        if (className == null || className.trim().isEmpty()) {
            Toast.makeText(this, "Invalid class name", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        className = className.replaceAll("[/\\\\]", "_");

        TextView classTitle = findViewById(R.id.classTitle);
        classTitle.setText("Class: " + className);

        presentCount = findViewById(R.id.presentCount);
        absentCount = findViewById(R.id.absentCount);
        saveButton = findViewById(R.id.saveButton);
        saveButton.setVisibility(View.GONE);

        RecyclerView studentRecycler = findViewById(R.id.studentListRecycler);
        studentRecycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new StudentAdapter(this, students, isEditable);
        adapter.setOnItemDoubleClickListener(this);
        studentRecycler.setAdapter(adapter);

        loadStudentsFromFirestore();

        // Swipe to delete with confirmation and reordering
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                AttenStudent student = students.get(position);
                String studentId = student.getId();

                new AlertDialog.Builder(ClassAttendanceActivity.this)
                        .setTitle("Delete Student")
                        .setMessage("Are you sure you want to delete this student?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            db.collection("classes")
                                    .document(className)
                                    .collection("students")
                                    .document(studentId)
                                    .delete()
                                    .addOnSuccessListener(aVoid -> {
                                        students.remove(position);

                                        // Reassign new IDs and update Firestore
                                        WriteBatch batch = db.batch();
                                        for (int i = 0; i < students.size(); i++) {
                                            AttenStudent s = students.get(i);
                                            s.setId(String.valueOf(i + 1));
                                            batch.set(db.collection("classes").document(className)
                                                    .collection("students").document(s.getId()), s);
                                        }
                                        batch.commit().addOnSuccessListener(unused -> {
                                            adapter.notifyDataSetChanged();
                                            updateCounts();
                                            Toast.makeText(ClassAttendanceActivity.this, "Student deleted", Toast.LENGTH_SHORT).show();
                                        }).addOnFailureListener(e -> {
                                            Toast.makeText(ClassAttendanceActivity.this, "Failed to reorder: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        });
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(ClassAttendanceActivity.this, "Delete failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        adapter.notifyItemChanged(position);
                                    });
                        })
                        .setNegativeButton("No", (dialog, which) -> {
                            adapter.notifyItemChanged(position);
                        })
                        .setCancelable(false)
                        .show();
            }
        });
        itemTouchHelper.attachToRecyclerView(studentRecycler);

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

        saveButton.setOnClickListener(v -> {
            isEditable = false;
            adapter.setEditable(false);
            saveAllStudentsToFirestore();
            saveButton.setVisibility(View.GONE);
        });

        updateCounts();
    }

    private void loadStudentsFromFirestore() {
        db.collection("classes")
                .document(className)
                .collection("students")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    students.clear();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        AttenStudent student = doc.toObject(AttenStudent.class);
                        student.setId(doc.getId());
                        students.add(student);
                    }
                    adapter.notifyDataSetChanged();
                    updateCounts();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to load students: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }

    private void showAddStudentDialog() {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            Toast.makeText(this, "Please sign in to add a student", Toast.LENGTH_LONG).show();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add New Student");

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_student_edit, null);
        EditText nameInput = dialogView.findViewById(R.id.editTextName);
        Spinner genderSpinner = dialogView.findViewById(R.id.spinnerGender);

        builder.setView(dialogView);
        builder.setPositiveButton("Add", null);
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();

        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setOnClickListener(v -> {
            String name = nameInput.getText().toString().trim();
            String gender = genderSpinner.getSelectedItem() != null ? genderSpinner.getSelectedItem().toString() : "";

            if (name.isEmpty()) {
                Toast.makeText(this, "Please enter a name", Toast.LENGTH_SHORT).show();
                return;
            }

            if (gender.isEmpty()) {
                Toast.makeText(this, "Please select a gender", Toast.LENGTH_SHORT).show();
                return;
            }

            AttenStudent newStudent = new AttenStudent("", name, gender, true);

            db.collection("classes")
                    .document(className)
                    .collection("students")
                    .add(newStudent)
                    .addOnSuccessListener(documentReference -> {
                        newStudent.setId(documentReference.getId());
                        students.add(newStudent);
                        adapter.notifyItemInserted(students.size() - 1);
                        updateCounts();
                        Toast.makeText(this, "Student added successfully", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Failed to add: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    });
        });
    }

    private void saveAllStudentsToFirestore() {
        WriteBatch batch = db.batch();
        for (AttenStudent student : students) {
            batch.set(db.collection("classes")
                    .document(className)
                    .collection("students")
                    .document(student.getId()), student);
        }
        batch.commit()
                .addOnSuccessListener(unused -> {
                    Toast.makeText(this, "Changes saved", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to save: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }

    @Override
    public void onItemDoubleClicked(int position) {
        AttenStudent student = students.get(position);
        student.setPresent(!student.isPresent());
        adapter.notifyItemChanged(position);

        db.collection("classes")
                .document(className)
                .collection("students")
                .document(student.getId())
                .update("present", student.isPresent())
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to update: " + e.getMessage(), Toast.LENGTH_LONG).show();
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
}
