package com.example.studentattendancemanagementapp;

import java.io.Serializable;

public class AttenStudentActivity implements Serializable {
    private int id;
    private String name;
    private String gender;
    private boolean isPresent;
    private boolean isSaved;

    // Constructor includes gender parameter now
    public AttenStudentActivity(int id, String name, String gender, boolean isPresent) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.isPresent = isPresent;
        this.isSaved = false;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public boolean isPresent() { return isPresent; }
    public void setPresent(boolean present) { this.isPresent= present; }
}