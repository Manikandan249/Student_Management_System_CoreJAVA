package com.studentms;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * StudentService.java — The SERVICE LAYER: all business logic lives here.
 *
 * WHY a separate service class?
 *   → Main.java handles the UI (what the user sees).
 *   → StudentService.java handles the logic (what the program does).
 *   → This separation is called the "Single Responsibility Principle" (SRP).
 *
 * In real projects this layer would talk to a database; here it uses an ArrayList.
 */
public class StudentService {

    // In-memory storage — the "database" for this application
    private final List<Student> studentList;

    // ─── Constructor ───────────────────────────────────────────────────────────
    public StudentService() {
        // Load any previously saved students from disk when the service starts
        this.studentList = FileHandler.loadFromFile();
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  1. ADD STUDENT
    // ══════════════════════════════════════════════════════════════════════════

    /**
     * Adds a new student after validating the ID is unique.
     *
     * @return true if added successfully, false if ID already exists.
     */
    public boolean addStudent(int id, String name, int age, String course, double marks) {
        // Validation: duplicate ID check
        if (isIdExists(id)) {
            System.out.println("  [✘] A student with ID " + id + " already exists.");
            return false;
        }
        studentList.add(new Student(id, name, age, course, marks));
        saveData();    // Persist immediately after every change
        System.out.println("  [✔] Student added successfully!");
        return true;
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  2. VIEW ALL STUDENTS
    // ══════════════════════════════════════════════════════════════════════════

    /**
     * Returns a copy of the student list (defensive copy — caller cannot mutate
     * the internal list directly, which is good OOP practice).
     */
    public List<Student> getAllStudents() {
        return new ArrayList<>(studentList);   // defensive copy
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  3. SEARCH BY ID
    // ══════════════════════════════════════════════════════════════════════════

    /**
     * Finds a student by ID.
     * Uses Optional<> to avoid NullPointerException — a modern Java best practice.
     *
     * @return Optional containing the student if found, or empty Optional if not.
     */
    public Optional<Student> findById(int id) {
        return studentList.stream()
                          .filter(s -> s.getId() == id)
                          .findFirst();
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  4. UPDATE STUDENT
    // ══════════════════════════════════════════════════════════════════════════

    /**
     * Updates a student's details (name, age, course, marks).
     * Empty/blank input means "keep the old value" — very user-friendly.
     *
     * @return true if the student was found and updated.
     */
    public boolean updateStudent(int id, String newName, String newAgeStr,
                                 String newCourse, String newMarksStr) {
        Optional<Student> result = findById(id);

        if (result.isEmpty()) {
            System.out.println("  [✘] No student found with ID " + id + ".");
            return false;
        }

        Student s = result.get();

        // Only update fields that the user provided (not blank)
        if (!newName.isBlank())    s.setName(newName);
        if (!newCourse.isBlank())  s.setCourse(newCourse);

        if (!newAgeStr.isBlank()) {
            try {
                int age = Integer.parseInt(newAgeStr);
                if (age < 1 || age > 120) throw new IllegalArgumentException("Age out of range");
                s.setAge(age);
            } catch (NumberFormatException e) {
                System.out.println("  [!] Invalid age input — age not updated.");
            }
        }

        if (!newMarksStr.isBlank()) {
            try {
                double marks = Double.parseDouble(newMarksStr);
                if (marks < 0 || marks > 100) throw new IllegalArgumentException("Marks 0–100 only");
                s.setMarks(marks);
            } catch (NumberFormatException e) {
                System.out.println("  [!] Invalid marks input — marks not updated.");
            }
        }

        saveData();
        System.out.println("  [✔] Student updated successfully!");
        return true;
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  5. DELETE STUDENT
    // ══════════════════════════════════════════════════════════════════════════

    /**
     * Removes a student by ID using the iterator-safe removeIf method.
     *
     * @return true if a record was deleted.
     */
    public boolean deleteStudent(int id) {
        boolean removed = studentList.removeIf(s -> s.getId() == id);
        if (removed) {
            saveData();
            System.out.println("  [✔] Student with ID " + id + " deleted.");
        } else {
            System.out.println("  [✘] No student found with ID " + id + ".");
        }
        return removed;
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  HELPER / UTILITY METHODS
    // ══════════════════════════════════════════════════════════════════════════

    /** Checks whether a student ID already exists in the list. */
    public boolean isIdExists(int id) {
        return studentList.stream().anyMatch(s -> s.getId() == id);
    }

    /** Returns the total number of students. */
    public int getTotalCount() {
        return studentList.size();
    }

    /**
     * Calculates the average marks across all students.
     * Returns 0.0 if there are no students (avoids division-by-zero).
     */
    public double getAverageMarks() {
        if (studentList.isEmpty()) return 0.0;
        return studentList.stream()
                          .mapToDouble(Student::getMarks)
                          .average()
                          .orElse(0.0);
    }

    /** Triggers file save — called internally after every data change. */
    private void saveData() {
        FileHandler.saveToFile(studentList);
    }
}
