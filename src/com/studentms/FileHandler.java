package com.studentms;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * FileHandler.java — Handles all file I/O operations.
 * Demonstrates FILE HANDLING: reading and writing student data to a .txt file.
 *
 * ABSTRACTION: The rest of the application never needs to know HOW data is
 * saved — it just calls save() and load().
 */
public class FileHandler {

    // Path to the data file (created in the same folder you run the program from)
    private static final String FILE_PATH = "students.txt";

    /**
     * Saves the entire student list to the file.
     * Overwrites the file each time (full snapshot approach — simple and reliable).
     *
     * @param students List of students to persist.
     */
    public static void saveToFile(List<Student> students) {
        // try-with-resources: BufferedWriter is automatically closed after the block
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Student s : students) {
                writer.write(s.toCsvLine());
                writer.newLine();   // OS-independent newline
            }
            System.out.println("  [✔] Data saved to '" + FILE_PATH + "' successfully.");
        } catch (IOException e) {
            // Catch I/O errors (disk full, permission denied, etc.)
            System.out.println("  [✘] Error saving data: " + e.getMessage());
        }
    }

    /**
     * Loads student records from the file into an ArrayList.
     * If the file doesn't exist yet, we silently return an empty list
     * (first-run scenario — perfectly normal).
     *
     * @return List of students loaded from file.
     */
    public static List<Student> loadFromFile() {
        List<Student> students = new ArrayList<>();
        File file = new File(FILE_PATH);

        // No file yet? That's fine — just start fresh.
        if (!file.exists()) {
            System.out.println("  [i] No existing data file found. Starting fresh.");
            return students;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {                     // skip blank lines
                    try {
                        students.add(Student.fromCsvLine(line));
                    } catch (Exception e) {
                        // Skip any corrupted lines instead of crashing
                        System.out.println("  [!] Skipping malformed line: " + line);
                    }
                }
            }
            System.out.println("  [✔] " + students.size() + " record(s) loaded from file.");
        } catch (IOException e) {
            System.out.println("  [✘] Error loading data: " + e.getMessage());
        }

        return students;
    }
}
