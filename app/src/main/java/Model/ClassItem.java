package Model;

public class ClassItem {
    private String id;
    private String name;
    private String classCode;  // If you have a third param

    // No-arg constructor (needed for Firestore or some serializers)
    public ClassItem() {}

    // 3-param constructor (your current one)
    public ClassItem(String id, String name, String classCode) {
        this.id = id;
        this.name = name;
        this.classCode = classCode;
    }

    // Add 2-param constructor if you want to keep your StudentsActivity code as is:
    public ClassItem(String id, String name) {
        this.id = id;
        this.name = name;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getClassCode() {
        return classCode;
    }

    // Setters (if needed)
    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }
}
