# Student Management System — Core Java

A console-based CRUD application built with Core Java.

## Features
- Add, View, Search, Update, Delete students
- File persistence (data survives program restart)
- Input validation and exception handling
- Menu-driven console UI

## Tech Stack
- Core Java (JDK 21)
- OOP — Encapsulation, Abstraction, SRP
- Collections — ArrayList, Optional, Streams
- File I/O — BufferedReader / BufferedWriter

## How to Run
```bash
javac -d out src/com/studentms/Student.java src/com/studentms/FileHandler.java src/com/studentms/StudentService.java src/com/studentms/Main.java
java -cp out com.studentms.Main
```

## Project Structure
```
src/com/studentms/
├── Student.java          # Model
├── FileHandler.java      # Persistence
├── StudentService.java   # Business Logic
└── Main.java             # UI Layer
```
