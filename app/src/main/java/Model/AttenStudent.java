package Model;

public class AttenStudent {
    private String id;
    private String name;
    private String gender;
    private boolean isPresent;

    // Required no-argument constructor for Firestore
    public AttenStudent() {
    }

    public AttenStudent(String id, String name, String gender, boolean isPresent) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.isPresent = isPresent;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public boolean isPresent() {
        return isPresent;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setPresent(boolean present) {
        isPresent = present;
    }
}
