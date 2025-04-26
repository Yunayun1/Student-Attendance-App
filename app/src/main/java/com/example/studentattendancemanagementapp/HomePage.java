package com.example.studentattendancemanagementapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class HomePage extends AppCompatActivity {

    TextView attendanceText, studentsText, joinClassText, profileText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        // Get views from layout
        TextView tvName = findViewById(R.id.teacherName);
        TextView tvRole = findViewById(R.id.teacherRole);

        // Get the user data passed from Login
        String name = getIntent().getStringExtra("name");
        String role = getIntent().getStringExtra("role");

        // Set name and role in UI
        if (name != null) {
            tvName.setText("Welcome, " + name + "!");
        }

        if (role != null) {
            tvRole.setText(" " + role);
        }

        // Initialize other views
        attendanceText = findViewById(R.id.attendanceText);
        studentsText = findViewById(R.id.studentsText);
        joinClassText = findViewById(R.id.joinClassText);
        profileText = findViewById(R.id.profileText);

        // Set click listeners
        attendanceText.setOnClickListener(v ->
                startActivity(new Intent(HomePage.this, AttendanceActivity.class)));

        studentsText.setOnClickListener(v ->
                startActivity(new Intent(HomePage.this, StudentsActivity.class)));

        joinClassText.setOnClickListener(v ->
                startActivity(new Intent(HomePage.this, JoinClassActivity.class)));

        profileText.setOnClickListener(v ->
               startActivity(new Intent(HomePage.this, ProfileActivity.class)));
    }
}