package com.example.studentattendancemanagementapp;

public class User {
    public String fullName;
    public String phone;
    public String email;
    public String role;

    public User() {
        // Required empty constructor for Firebase
    }

    public User(String fullName, String phone, String email, String role) {
        this.fullName = fullName;
        this.phone = phone;
        this.email = email;
        this.role = role;
    }
}
