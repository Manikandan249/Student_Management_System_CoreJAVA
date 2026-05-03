package com.studentms;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/**
 * Main.java — Entry point and UI layer (console menu).
 *
 * Responsibilities:
 *   → Display the menu to the user.
 *   → Read and validate user input.
 *   → Delegate ALL business logic to StudentService.
 *   → Display results back to the user.
 *
 * Think of this as the "Controller" in an MVC pattern.
 */
public class Main {

    // ─── Constants ─────────────────────────────────────────────────────────────
    private static final String DIVIDER     = "─".repeat(62);
    private static final String HEADER_LINE =
        "| " + String.format("%-4s", "ID") + " | " +
        String.format("%-20s", "Name")     + " | " +
        String.format("%-3s", "Age")       + " | " +
        String.format("%-15s", "Course")   + " | " +
        String.format("%-6s", "Marks")     + " |";

    // ─── Shared instances ───────────────────────────────────────────────────────
    private static final Scanner        scanner = new Scanner(System.in);
    private static final StudentService service = new StudentService();

    // ══════════════════════════════════════════════════════════════════════════
    //  MAIN METHOD
    // ══════════════════════════════════════════════════════════════════════════
    public static void main(String[] args) {
        printBanner();

        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt("Enter your choice: ");

            // try-catch wraps the entire switch to handle any unexpected errors
            try {
                switch (choice) {
                    case 1  -> handleAddStudent();
                    case 2  -> handleViewAll();
                    case 3  -> handleSearchById();
                    case 4  -> handleUpdateStudent();
                    case 5  -> handleDeleteStudent();
                    case 6  -> handleStats();
                    case 0  -> { running = false; handleExit(); }
                    default -> System.out.println("\n  [!] Invalid choice. Please enter 0–6.\n");
                }
            } catch (Exception e) {
                // Safety net — catches anything we didn't anticipate
                System.out.println("\n  [✘] Unexpected error: " + e.getMessage() + "\n");
            }
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  MENU HANDLERS
    // ══════════════════════════════════════════════════════════════════════════

    /** 1. Add a new student */
    private static void handleAddStudent() {
        printSectionHeader("ADD NEW STUDENT");

        int id = readInt("  Student ID     : ");

        // Early exit if ID already taken — no point asking for the rest
        if (service.isIdExists(id)) {
            System.out.println("  [✘] ID " + id + " is already taken. Returning to menu.\n");
            return;
        }

        String name   = readNonEmpty("  Full Name       : ");
        int    age    = readIntInRange("  Age (1–120)     : ", 1, 120);
        String course = readNonEmpty("  Course          : ");
        double marks  = readDoubleInRange("  Marks (0–100)   : ", 0, 100);

        System.out.println();
        service.addStudent(id, name, age, course, marks);
        System.out.println();
    }

    /** 2. View all students */
    private static void handleViewAll() {
        printSectionHeader("ALL STUDENTS");
        List<Student> students = service.getAllStudents();

        if (students.isEmpty()) {
            System.out.println("  No students found. Add some first!\n");
            return;
        }

        printTableHeader();
        students.forEach(s -> System.out.println(s));
        System.out.println(DIVIDER);
        System.out.printf("  Total: %d student(s)%n%n", students.size());
    }

    /** 3. Search student by ID */
    private static void handleSearchById() {
        printSectionHeader("SEARCH BY ID");
        int id = readInt("  Enter Student ID: ");

        Optional<Student> result = service.findById(id);

        if (result.isPresent()) {
            System.out.println("\n  Student found:");
            printTableHeader();
            System.out.println(result.get());
            System.out.println(DIVIDER);
        } else {
            System.out.println("  [✘] No student found with ID " + id + ".");
        }
        System.out.println();
    }

    /** 4. Update student details */
    private static void handleUpdateStudent() {
        printSectionHeader("UPDATE STUDENT");
        int id = readInt("  Enter Student ID to update: ");

        // Show current data first so the user knows what they're editing
        Optional<Student> existing = service.findById(id);
        if (existing.isEmpty()) {
            System.out.println("  [✘] Student not found.\n");
            return;
        }

        System.out.println("\n  Current record:");
        printTableHeader();
        System.out.println(existing.get());
        System.out.println(DIVIDER);
        System.out.println("  (Press ENTER to keep the current value)\n");

        String newName    = readOptional("  New Name    : ");
        String newAge     = readOptional("  New Age     : ");
        String newCourse  = readOptional("  New Course  : ");
        String newMarks   = readOptional("  New Marks   : ");

        System.out.println();
        service.updateStudent(id, newName, newAge, newCourse, newMarks);
        System.out.println();
    }

    /** 5. Delete a student */
    private static void handleDeleteStudent() {
        printSectionHeader("DELETE STUDENT");
        int id = readInt("  Enter Student ID to delete: ");

        // Show record before asking to confirm
        Optional<Student> student = service.findById(id);
        if (student.isEmpty()) {
            System.out.println("  [✘] Student not found.\n");
            return;
        }

        System.out.println("\n  Record to delete:");
        printTableHeader();
        System.out.println(student.get());
        System.out.println(DIVIDER);

        System.out.print("  Are you sure? (yes/no): ");
        String confirm = scanner.nextLine().trim().toLowerCase();

        System.out.println();
        if (confirm.equals("yes") || confirm.equals("y")) {
            service.deleteStudent(id);
        } else {
            System.out.println("  [i] Deletion cancelled.");
        }
        System.out.println();
    }

    /** 6. Show quick statistics */
    private static void handleStats() {
        printSectionHeader("STATISTICS");
        System.out.printf("  %-25s : %d%n",    "Total Students",   service.getTotalCount());
        System.out.printf("  %-25s : %.2f%%%n", "Average Marks",    service.getAverageMarks());
        System.out.println();
    }

    /** 0. Exit */
    private static void handleExit() {
        System.out.println("\n  Goodbye! Your data has been saved. See you next time!");
        System.out.println("  " + DIVIDER);
        scanner.close();
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  INPUT HELPERS — all Scanner calls go through these methods
    //  so validation logic is centralised (DRY principle)
    // ══════════════════════════════════════════════════════════════════════════

    /**
     * Reads an integer. Keeps prompting until the user enters a valid integer.
     */
    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("  [!] Please enter a whole number.");
            }
        }
    }

    /**
     * Reads an integer within [min, max]. Keeps prompting until valid.
     */
    private static int readIntInRange(String prompt, int min, int max) {
        while (true) {
            int value = readInt(prompt);
            if (value >= min && value <= max) return value;
            System.out.printf("  [!] Please enter a number between %d and %d.%n", min, max);
        }
    }

    /**
     * Reads a double within [min, max]. Keeps prompting until valid.
     */
    private static double readDoubleInRange(String prompt, double min, double max) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                double value = Double.parseDouble(input);
                if (value >= min && value <= max) return value;
                System.out.printf("  [!] Please enter a value between %.1f and %.1f.%n", min, max);
            } catch (NumberFormatException e) {
                System.out.println("  [!] Please enter a valid number.");
            }
        }
    }

    /**
     * Reads a non-empty String. Keeps prompting until the user types something.
     */
    private static String readNonEmpty(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) return input;
            System.out.println("  [!] This field cannot be empty.");
        }
    }

    /**
     * Reads an optional String — empty input is allowed (used for updates).
     */
    private static String readOptional(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  DISPLAY HELPERS
    // ══════════════════════════════════════════════════════════════════════════

    private static void printBanner() {
        System.out.println("\n  " + DIVIDER);
        System.out.println("  |       STUDENT MANAGEMENT SYSTEM  v1.0              |");
        System.out.println("  |       Built with Core Java  •  OOP  •  File I/O    |");
        System.out.println("  " + DIVIDER + "\n");
    }

    private static void printMenu() {
        System.out.println("  " + DIVIDER);
        System.out.println("  MAIN MENU");
        System.out.println("  " + DIVIDER);
        System.out.println("    1. Add New Student");
        System.out.println("    2. View All Students");
        System.out.println("    3. Search Student by ID");
        System.out.println("    4. Update Student Details");
        System.out.println("    5. Delete Student");
        System.out.println("    6. View Statistics");
        System.out.println("    0. Exit");
        System.out.println("  " + DIVIDER);
    }

    private static void printSectionHeader(String title) {
        System.out.println("\n  ── " + title + " " + "─".repeat(Math.max(0, 50 - title.length())));
    }

    private static void printTableHeader() {
        System.out.println("  " + DIVIDER);
        System.out.println("  " + HEADER_LINE);
        System.out.println("  " + DIVIDER);
    }
}
