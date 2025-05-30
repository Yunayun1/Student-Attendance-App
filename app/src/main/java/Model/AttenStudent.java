package Model;

public class AttenStudent {
    private int id;
    private String name;
    private String gender;
    private boolean isPresent;

    public AttenStudent(int id, String name, String gender, boolean isPresent) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.isPresent = isPresent;
    }

    // getters and setters
    public int getId() { return id; }
    public String getName() { return name; }
    public void setId(int id) {
        this.id = id;
    }

    public String getGender() { return gender; }
    public boolean isPresent() { return isPresent; }
    public void setPresent(boolean present) { isPresent = present; }
}