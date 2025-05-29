package com.example.studentattendancemanagementapp.model;

public class AttenStudent {

    private int id;
    private String name;
    private String gender;
    private boolean present;
    private boolean saved; // this is needed for setSaved()

    // Add gender to constructor parameters
    public AttenStudent(int id, String name, String gender, boolean present) {
        this.id = id;
        this.name = name;
        this.gender = gender; // assign passed gender here
        this.present = present;
        this.saved = false;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public boolean isPresent() {
        return present;
    }

    public void setPresent(boolean present) {
        this.present = present;
    }

    public boolean isSaved() {
        return saved;
    }

    public void setSaved(boolean saved) {
        this.saved = saved;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
