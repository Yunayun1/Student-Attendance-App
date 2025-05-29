package com.example.studentattendancemanagementapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;

public class HomePage extends AppCompatActivity {

    TextView attendanceText, studentsText, joinClassText, profileText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        //  Get views from layout
        TextView tvName = findViewById(R.id.teacherName); // "Welcome, ..."
        TextView tvRole = findViewById(R.id.teacherRole); // "Role: ..."

        //  Get name and role from SharedPreferences instead of Intent
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String name = prefs.getString("name", null);
        String role = prefs.getString("role", null);

        //  Set name and role in UI
        if (name != null) {
            tvName.setText("Welcome, " + name + "!");
        } else {
            tvName.setText("Welcome!");
        }

        if (role != null) {
            tvRole.setText(role);
        } else {
            tvRole.setText("Unknown");
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