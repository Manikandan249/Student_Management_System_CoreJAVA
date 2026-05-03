# Student Management System — Core Java
### A beginner-friendly, interview-ready project using OOP, Collections & File I/O

---

## Folder Structure

```
StudentManagementSystem/
│
├── src/
│   └── com/
│       └── studentms/
│           ├── Student.java          ← Model (data / entity)
│           ├── FileHandler.java      ← Persistence (file I/O)
│           ├── StudentService.java   ← Service (business logic)
│           └── Main.java             ← UI (menu + input handling)
│
├── out/                              ← Compiled .class files (auto-created)
│   └── com/studentms/...
│
└── students.txt                      ← Data file (auto-created on first save)
```

**Why this structure?**
Each class has ONE job (Single Responsibility Principle):
- `Student`        → Knows how to represent a student (data + CSV conversion)
- `FileHandler`    → Knows how to read/write the data file
- `StudentService` → Knows the business rules (no duplicate IDs, validations)
- `Main`           → Knows how to talk to the user (menus, Scanner)

---

## How to Compile & Run

### Step 1 — Compile (from the project root folder)
```bash
javac -d out src/com/studentms/*.java
```
This compiles all 4 classes into the `out/` directory.

### Step 2 — Run
```bash
java -cp out com.studentms.Main
```

### One-liner (compile + run)
```bash
javac -d out src/com/studentms/*.java && java -cp out com.studentms.Main
```

> **Note:** The `students.txt` data file is created automatically in whatever
> directory you run the program from. No setup needed.

---

## Sample Console Output

```
  ──────────────────────────────────────────────────────────────
  |       STUDENT MANAGEMENT SYSTEM  v1.0                      |
  |       Built with Core Java  •  OOP  •  File I/O            |
  ──────────────────────────────────────────────────────────────

  [i] No existing data file found. Starting fresh.

  ──────────────────────────────────────────────────────────────
  MAIN MENU
  ──────────────────────────────────────────────────────────────
    1. Add New Student
    2. View All Students
    3. Search Student by ID
    4. Update Student Details
    5. Delete Student
    6. View Statistics
    0. Exit
  ──────────────────────────────────────────────────────────────
Enter your choice: 1

  ── ADD NEW STUDENT ────────────────────────────────────────────
  Student ID     : 101
  Full Name       : Alice Johnson
  Age (1–120)     : 20
  Course          : Computer Science
  Marks (0–100)   : 88.5

  [✔] Data saved to 'students.txt' successfully.
  [✔] Student added successfully!

Enter your choice: 2

  ── ALL STUDENTS ───────────────────────────────────────────────
  ──────────────────────────────────────────────────────────────
  | ID   | Name                 | Age | Course          | Marks  |
  ──────────────────────────────────────────────────────────────
  | 101  | Alice Johnson        | 20  | Computer Science | 88.50  |
  | 102  | Bob Smith            | 22  | Mathematics      | 74.00  |
  | 103  | Priya Patel          | 21  | Data Science     | 92.00  |
  ──────────────────────────────────────────────────────────────
  Total: 3 student(s)

Enter your choice: 6

  ── STATISTICS ─────────────────────────────────────────────────
  Total Students            : 3
  Average Marks             : 84.83%
```

---

## students.txt (auto-generated data file)

```
101,Alice Johnson,20,Computer Science,88.5
102,Bob Smith,22,Mathematics,74.0
103,Priya Patel,21,Data Science,92.0
```
Plain CSV format — easy to open in Excel, easy to parse, easy to debug.

---

## OOP Concepts Used (for your interview)

| Concept         | Where it appears                                              |
|-----------------|---------------------------------------------------------------|
| Encapsulation   | `Student` fields are `private`, accessed via getters/setters |
| Abstraction     | `FileHandler.save/load()` hide how I/O actually works        |
| SRP             | Each class has exactly one job                               |
| Optional<>      | `findById()` returns Optional to avoid NullPointerException  |
| Defensive copy  | `getAllStudents()` returns a new list, not the internal one   |
| try-with-resources | BufferedWriter/Reader auto-closed — no resource leaks     |

---

## Interview Talking Points — What to Mention

### You can say:
> *"I separated the project into three layers: Model, Service, and UI —
> similar to how real MVC applications are structured."*

> *"I used Optional instead of returning null, because null can cause
> NullPointerExceptions that are hard to debug."*

> *"The file handler uses try-with-resources, which automatically closes
> the file even if an exception is thrown, preventing resource leaks."*

> *"I used defensive copying in getAllStudents() — returning a copy of
> the list instead of a reference to it, so external code can't accidentally
> corrupt the internal state of the service."*

---

## Suggested Next Improvements (for bonus interview points)

1. **Sorting** — Sort students by name, marks, or ID using `Comparator`
2. **Search by name** — Case-insensitive partial name search using `Stream.filter`
3. **Grade calculation** — Auto-compute grades (A/B/C) based on marks
4. **JSON storage** — Use Gson/Jackson instead of plain CSV (shows library knowledge)
5. **Unit tests** — Add JUnit 5 tests for `StudentService` methods
6. **Database** — Replace `FileHandler` with JDBC + SQLite (same interface, different implementation — demonstrates the power of abstraction)
7. **Generics** — Abstract `StudentService` into a generic `CrudService<T>` that any entity type can use
