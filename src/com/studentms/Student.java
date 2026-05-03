package com.studentms;

/**
 * Student.java — Model class representing a single Student entity.
 * Demonstrates ENCAPSULATION: all fields are private, accessed via getters/setters.
 */
public class Student {

    // ─── Fields ────────────────────────────────────────────────────────────────
    private int    id;
    private String name;
    private int    age;
    private String course;
    private double marks;

    // ─── Constructor ───────────────────────────────────────────────────────────
    public Student(int id, String name, int age, String course, double marks) {
        this.id     = id;
        this.name   = name;
        this.age    = age;
        this.course = course;
        this.marks  = marks;
    }

    // ─── Getters ───────────────────────────────────────────────────────────────
    public int    getId()     { return id; }
    public String getName()   { return name; }
    public int    getAge()    { return age; }
    public String getCourse() { return course; }
    public double getMarks()  { return marks; }

    // ─── Setters (ID intentionally excluded — IDs should not change) ───────────
    public void setName(String name)     { this.name   = name; }
    public void setAge(int age)          { this.age    = age; }
    public void setCourse(String course) { this.course = course; }
    public void setMarks(double marks)   { this.marks  = marks; }

    /**
     * Converts the student object to a CSV line for file storage.
     * Format: id,name,age,course,marks
     */
    public String toCsvLine() {
        return id + "," + name + "," + age + "," + course + "," + marks;
    }

    /**
     * Recreates a Student object from a CSV line (used when loading from file).
     */
    public static Student fromCsvLine(String csvLine) {
        String[] parts = csvLine.split(",");
        int    id     = Integer.parseInt(parts[0].trim());
        String name   = parts[1].trim();
        int    age    = Integer.parseInt(parts[2].trim());
        String course = parts[3].trim();
        double marks  = Double.parseDouble(parts[4].trim());
        return new Student(id, name, age, course, marks);
    }

    /**
     * Readable string output — used when printing student details to console.
     */
    @Override
    public String toString() {
        return String.format(
            "| %-4d | %-20s | %-3d | %-15s | %-6.2f |",
            id, name, age, course, marks
        );
    }
}
