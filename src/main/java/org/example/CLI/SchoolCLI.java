package org.example.CLI;

import org.example.DAO.*;
import org.example.Datebase.DDL;
import org.example.Model.*;
import org.example.Service.SchoolService;

import java.util.List;
import java.util.Scanner;

public class SchoolCLI {
    private final Scanner scanner;
    private final SchoolService schoolService;

    public SchoolCLI() {
        this.scanner = new Scanner(System.in);
        // Initialize DAOs
        TeacherDAO teacherDAO = new TeacherDAO();
        StudentDAO studentDAO = new StudentDAO();
        SubjectDAO subjectDAO = new SubjectDAO();
        GradeDAO gradeDAO = new GradeDAO();
        EnrollmentDAO enrollmentDAO = new EnrollmentDAO();
        
        // Initialize service
        this.schoolService = new SchoolService(teacherDAO, studentDAO, subjectDAO, gradeDAO, enrollmentDAO);
    }

    public void start() {
        // Initialize database
        System.out.println("Initializing database...");
        DDL.createDatabase();
        DDL.createTables();
        System.out.println("Database ready!\n");

        boolean running = true;
        while (running) {
            showMainMenu();
            int choice = getIntInput("Enter your choice: ");
            
            switch (choice) {
                case 1 -> handleStudentOperations();
                case 2 -> handleTeacherOperations();
                case 3 -> handleSubjectOperations();
                case 4 -> handleEnrollmentOperations();
                case 5 -> handleGradeOperations();
                case 6 -> handleReportOperations();
                case 0 -> {
                    System.out.println("Thank you for using School Management System. Goodbye!");
                    running = false;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
        scanner.close();
    }

    private void showMainMenu() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("    SCHOOL MANAGEMENT SYSTEM");
        System.out.println("=".repeat(50));
        System.out.println("1. Student Management");
        System.out.println("2. Teacher Management");
        System.out.println("3. Subject Management");
        System.out.println("4. Enrollment Management");
        System.out.println("5. Grade Management");
        System.out.println("6. Reports");
        System.out.println("0. Exit");
        System.out.println("=".repeat(50));
    }

    // ==================== Student Operations ====================
    private void handleStudentOperations() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Student Management ---");
            System.out.println("1. Add Student");
            System.out.println("2. View All Students");
            System.out.println("3. View Student by ID");
            System.out.println("4. Search Students by Name");
            System.out.println("5. Update Student");
            System.out.println("6. Delete Student");
            System.out.println("0. Back to Main Menu");
            
            int choice = getIntInput("Enter your choice: ");
            
            switch (choice) {
                case 1 -> addStudent();
                case 2 -> viewAllStudents();
                case 3 -> viewStudentById();
                case 4 -> searchStudentsByName();
                case 5 -> updateStudent();
                case 6 -> deleteStudent();
                case 0 -> back = true;
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void addStudent() {
        System.out.println("\n--- Add New Student ---");
        String name = getStringInput("Enter name: ");
        String email = getStringInput("Enter email: ");
        int age = getIntInput("Enter age: ");
        
        try {
            Student student = new Student(name, email, age);
            schoolService.createStudent(student);
            System.out.println("✓ Student added successfully!");
        } catch (Exception e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
    }

    private void viewAllStudents() {
        System.out.println("\n--- All Students ---");
        try {
            List<Student> students = schoolService.getAllStudents();
            if (students.isEmpty()) {
                System.out.println("No students found.");
            } else {
                System.out.printf("%-5s %-20s %-30s %-5s%n", "ID", "Name", "Email", "Age");
                System.out.println("-".repeat(65));
                for (Student s : students) {
                    System.out.printf("%-5d %-20s %-30s %-5d%n", 
                        s.getId(), s.getName(), s.getEmail(), s.getAge());
                }
            }
        } catch (Exception e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
    }

    private void viewStudentById() {
        int id = getIntInput("Enter student ID: ");
        try {
            Student student = schoolService.getStudentById(id);
            System.out.println("\n--- Student Details ---");
            System.out.println("ID: " + student.getId());
            System.out.println("Name: " + student.getName());
            System.out.println("Email: " + student.getEmail());
            System.out.println("Age: " + student.getAge());
        } catch (Exception e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
    }

    private void searchStudentsByName() {
        String name = getStringInput("Enter name to search: ");
        try {
            List<Student> students = schoolService.getStudentByName(name);
            if (students.isEmpty()) {
                System.out.println("No students found with name containing: " + name);
            } else {
                System.out.println("\n--- Search Results ---");
                System.out.printf("%-5s %-20s %-30s %-5s%n", "ID", "Name", "Email", "Age");
                System.out.println("-".repeat(65));
                for (Student s : students) {
                    System.out.printf("%-5d %-20s %-30s %-5d%n", 
                        s.getId(), s.getName(), s.getEmail(), s.getAge());
                }
            }
        } catch (Exception e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
    }

    private void updateStudent() {
        int id = getIntInput("Enter student ID to update: ");
        try {
            Student student = schoolService.getStudentById(id);
            System.out.println("Current details:");
            System.out.println("Name: " + student.getName());
            System.out.println("Email: " + student.getEmail());
            System.out.println("Age: " + student.getAge());
            
            System.out.println("\nEnter new details (press Enter to keep current value):");
            String name = getStringInput("Name [" + student.getName() + "]: ");
            String email = getStringInput("Email [" + student.getEmail() + "]: ");
            String ageStr = getStringInput("Age [" + student.getAge() + "]: ");
            
            if (!name.isEmpty()) student.setName(name);
            if (!email.isEmpty()) student.setEmail(email);
            if (!ageStr.isEmpty()) student.setAge(Integer.parseInt(ageStr));
            
            schoolService.updateStudent(student);
            System.out.println("✓ Student updated successfully!");
        } catch (Exception e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
    }

    private void deleteStudent() {
        int id = getIntInput("Enter student ID to delete: ");
        try {
            Student student = schoolService.getStudentById(id);
            System.out.println("Are you sure you want to delete student: " + student.getName() + "? (yes/no)");
            String confirm = scanner.nextLine().trim().toLowerCase();
            if (confirm.equals("yes")) {
                schoolService.deleteStudent(id);
                System.out.println("✓ Student deleted successfully!");
            } else {
                System.out.println("Deletion cancelled.");
            }
        } catch (Exception e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
    }

    // ==================== Teacher Operations ====================
    private void handleTeacherOperations() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Teacher Management ---");
            System.out.println("1. Add Teacher");
            System.out.println("2. View All Teachers");
            System.out.println("3. View Teacher by ID");
            System.out.println("4. Search Teachers by Name");
            System.out.println("5. Update Teacher");
            System.out.println("6. Delete Teacher");
            System.out.println("0. Back to Main Menu");
            
            int choice = getIntInput("Enter your choice: ");
            
            switch (choice) {
                case 1 -> addTeacher();
                case 2 -> viewAllTeachers();
                case 3 -> viewTeacherById();
                case 4 -> searchTeachersByName();
                case 5 -> updateTeacher();
                case 6 -> deleteTeacher();
                case 0 -> back = true;
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void addTeacher() {
        System.out.println("\n--- Add New Teacher ---");
        String name = getStringInput("Enter name: ");
        String specialization = getStringInput("Enter specialization: ");
        
        try {
            Teacher teacher = new Teacher(name, specialization);
            schoolService.createTeacher(teacher);
            System.out.println("✓ Teacher added successfully!");
        } catch (Exception e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
    }

    private void viewAllTeachers() {
        System.out.println("\n--- All Teachers ---");
        try {
            List<Teacher> teachers = schoolService.getAll();
            if (teachers.isEmpty()) {
                System.out.println("No teachers found.");
            } else {
                System.out.printf("%-5s %-20s %-30s%n", "ID", "Name", "Specialization");
                System.out.println("-".repeat(60));
                for (Teacher t : teachers) {
                    System.out.printf("%-5d %-20s %-30s%n", 
                        t.getId(), t.getName(), t.getSpecialization());
                }
            }
        } catch (Exception e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
    }

    private void viewTeacherById() {
        int id = getIntInput("Enter teacher ID: ");
        try {
            Teacher teacher = schoolService.getByTeacherId(id);
            System.out.println("\n--- Teacher Details ---");
            System.out.println("ID: " + teacher.getId());
            System.out.println("Name: " + teacher.getName());
            System.out.println("Specialization: " + teacher.getSpecialization());
            
            // Show subjects taught by this teacher
            List<Subject> subjects = schoolService.getSubjectByTeacherId(id);
            if (!subjects.isEmpty()) {
                System.out.println("\nSubjects taught:");
                for (Subject s : subjects) {
                    System.out.println("  - " + s.getName() + " (ID: " + s.getId() + ")");
                }
            }
        } catch (Exception e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
    }

    private void searchTeachersByName() {
        String name = getStringInput("Enter name to search: ");
        try {
            List<Teacher> teachers = schoolService.getByTeacherName(name);
            if (teachers.isEmpty()) {
                System.out.println("No teachers found with name containing: " + name);
            } else {
                System.out.println("\n--- Search Results ---");
                System.out.printf("%-5s %-20s %-30s%n", "ID", "Name", "Specialization");
                System.out.println("-".repeat(60));
                for (Teacher t : teachers) {
                    System.out.printf("%-5d %-20s %-30s%n", 
                        t.getId(), t.getName(), t.getSpecialization());
                }
            }
        } catch (Exception e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
    }

    private void updateTeacher() {
        int id = getIntInput("Enter teacher ID to update: ");
        try {
            Teacher teacher = schoolService.getByTeacherId(id);
            System.out.println("Current details:");
            System.out.println("Name: " + teacher.getName());
            System.out.println("Specialization: " + teacher.getSpecialization());
            
            System.out.println("\nEnter new details (press Enter to keep current value):");
            String name = getStringInput("Name [" + teacher.getName() + "]: ");
            String specialization = getStringInput("Specialization [" + teacher.getSpecialization() + "]: ");
            
            if (!name.isEmpty()) teacher.setName(name);
            if (!specialization.isEmpty()) teacher.setSpecialization(specialization);
            
            schoolService.updateTeacher(teacher);
            System.out.println("✓ Teacher updated successfully!");
        } catch (Exception e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
    }

    private void deleteTeacher() {
        int id = getIntInput("Enter teacher ID to delete: ");
        try {
            Teacher teacher = schoolService.getByTeacherId(id);
            System.out.println("Are you sure you want to delete teacher: " + teacher.getName() + "? (yes/no)");
            String confirm = scanner.nextLine().trim().toLowerCase();
            if (confirm.equals("yes")) {
                schoolService.deleteTeacher(id);
                System.out.println("✓ Teacher deleted successfully!");
            } else {
                System.out.println("Deletion cancelled.");
            }
        } catch (Exception e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
    }

    // ==================== Subject Operations ====================
    private void handleSubjectOperations() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Subject Management ---");
            System.out.println("1. Add Subject");
            System.out.println("2. View All Subjects");
            System.out.println("3. View Subject by ID");
            System.out.println("4. View Subjects by Teacher");
            System.out.println("5. Update Subject");
            System.out.println("6. Delete Subject");
            System.out.println("0. Back to Main Menu");
            
            int choice = getIntInput("Enter your choice: ");
            
            switch (choice) {
                case 1 -> addSubject();
                case 2 -> viewAllSubjects();
                case 3 -> viewSubjectById();
                case 4 -> viewSubjectsByTeacher();
                case 5 -> updateSubject();
                case 6 -> deleteSubject();
                case 0 -> back = true;
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void addSubject() {
        System.out.println("\n--- Add New Subject ---");
        String name = getStringInput("Enter subject name: ");
        int teacherId = getIntInput("Enter teacher ID: ");
        
        try {
            // Verify teacher exists
            schoolService.getByTeacherId(teacherId);
            Subject subject = new Subject(name, teacherId);
            schoolService.createSubject(subject);
            System.out.println("✓ Subject added successfully!");
        } catch (Exception e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
    }

    private void viewAllSubjects() {
        System.out.println("\n--- All Subjects ---");
        try {
            List<Subject> subjects = schoolService.getAllSubjects();
            if (subjects.isEmpty()) {
                System.out.println("No subjects found.");
            } else {
                System.out.printf("%-5s %-25s %-10s%n", "ID", "Name", "Teacher ID");
                System.out.println("-".repeat(45));
                for (Subject s : subjects) {
                    System.out.printf("%-5d %-25s %-10d%n", 
                        s.getId(), s.getName(), s.getTeacherId());
                }
            }
        } catch (Exception e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
    }

    private void viewSubjectById() {
        int id = getIntInput("Enter subject ID: ");
        try {
            Subject subject = schoolService.getSubjectById(id);
            System.out.println("\n--- Subject Details ---");
            System.out.println("ID: " + subject.getId());
            System.out.println("Name: " + subject.getName());
            System.out.println("Teacher ID: " + subject.getTeacherId());
            
            try {
                Teacher teacher = schoolService.getByTeacherId(subject.getTeacherId());
                System.out.println("Teacher: " + teacher.getName());
            } catch (Exception e) {
                System.out.println("Teacher: Not found");
            }
        } catch (Exception e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
    }

    private void viewSubjectsByTeacher() {
        int teacherId = getIntInput("Enter teacher ID: ");
        try {
            schoolService.getByTeacherId(teacherId); // Verify teacher exists
            List<Subject> subjects = schoolService.getSubjectByTeacherId(teacherId);
            if (subjects.isEmpty()) {
                System.out.println("No subjects found for this teacher.");
            } else {
                System.out.println("\n--- Subjects ---");
                System.out.printf("%-5s %-25s%n", "ID", "Name");
                System.out.println("-".repeat(35));
                for (Subject s : subjects) {
                    System.out.printf("%-5d %-25s%n", s.getId(), s.getName());
                }
            }
        } catch (Exception e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
    }

    private void updateSubject() {
        int id = getIntInput("Enter subject ID to update: ");
        try {
            Subject subject = schoolService.getSubjectById(id);
            System.out.println("Current details:");
            System.out.println("Name: " + subject.getName());
            System.out.println("Teacher ID: " + subject.getTeacherId());
            
            System.out.println("\nEnter new details (press Enter to keep current value):");
            String name = getStringInput("Name [" + subject.getName() + "]: ");
            String teacherIdStr = getStringInput("Teacher ID [" + subject.getTeacherId() + "]: ");
            
            if (!name.isEmpty()) subject.setName(name);
            if (!teacherIdStr.isEmpty()) {
                int teacherId = Integer.parseInt(teacherIdStr);
                schoolService.getByTeacherId(teacherId); // Verify teacher exists
                subject.setTeacherId(teacherId);
            }
            
            schoolService.updateSubject(subject);
            System.out.println("✓ Subject updated successfully!");
        } catch (Exception e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
    }

    private void deleteSubject() {
        int id = getIntInput("Enter subject ID to delete: ");
        try {
            Subject subject = schoolService.getSubjectById(id);
            System.out.println("Are you sure you want to delete subject: " + subject.getName() + "? (yes/no)");
            String confirm = scanner.nextLine().trim().toLowerCase();
            if (confirm.equals("yes")) {
                schoolService.deleteSubjectById(id);
                System.out.println("✓ Subject deleted successfully!");
            } else {
                System.out.println("Deletion cancelled.");
            }
        } catch (Exception e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
    }

    // ==================== Enrollment Operations ====================
    private void handleEnrollmentOperations() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Enrollment Management ---");
            System.out.println("1. Enroll Student to Subject");
            System.out.println("2. View All Enrollments");
            System.out.println("3. View Enrollments by Student");
            System.out.println("4. View Enrollments by Subject");
            System.out.println("0. Back to Main Menu");
            
            int choice = getIntInput("Enter your choice: ");
            
            switch (choice) {
                case 1 -> enrollStudentToSubject();
                case 2 -> viewAllEnrollments();
                case 3 -> viewEnrollmentsByStudent();
                case 4 -> viewEnrollmentsBySubject();
                case 0 -> back = true;
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void enrollStudentToSubject() {
        System.out.println("\n--- Enroll Student to Subject ---");
        int studentId = getIntInput("Enter student ID: ");
        int subjectId = getIntInput("Enter subject ID: ");
        
        try {
            // Verify student and subject exist
            schoolService.getStudentById(studentId);
            schoolService.getSubjectById(subjectId);
            
            schoolService.enrollStudentToSubject(studentId, subjectId);
            System.out.println("✓ Student enrolled successfully!");
        } catch (Exception e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
    }

    private void viewAllEnrollments() {
        System.out.println("\n--- All Enrollments ---");
        try {
            List<Enrollment> enrollments = schoolService.getAllEnrollments();
            if (enrollments.isEmpty()) {
                System.out.println("No enrollments found.");
            } else {
                System.out.printf("%-5s %-10s %-10s%n", "ID", "Student ID", "Subject ID");
                System.out.println("-".repeat(30));
                for (Enrollment e : enrollments) {
                    System.out.printf("%-5d %-10d %-10d%n", 
                        e.getId(), e.getStudentId(), e.getSubjectId());
                }
            }
        } catch (Exception e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
    }

    private void viewEnrollmentsByStudent() {
        int studentId = getIntInput("Enter student ID: ");
        try {
            schoolService.getStudentById(studentId); // Verify student exists
            List<Enrollment> enrollments = schoolService.getEnrollmentsByStudentId(studentId);
            if (enrollments.isEmpty()) {
                System.out.println("No enrollments found for this student.");
            } else {
                System.out.println("\n--- Enrollments ---");
                System.out.printf("%-5s %-10s%n", "ID", "Subject ID");
                System.out.println("-".repeat(20));
                for (Enrollment e : enrollments) {
                    System.out.printf("%-5d %-10d%n", e.getId(), e.getSubjectId());
                }
            }
        } catch (Exception e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
    }

    private void viewEnrollmentsBySubject() {
        int subjectId = getIntInput("Enter subject ID: ");
        try {
            schoolService.getSubjectById(subjectId); // Verify subject exists
            List<Enrollment> enrollments = schoolService.getEnrollmentsBySubjectId(subjectId);
            if (enrollments.isEmpty()) {
                System.out.println("No enrollments found for this subject.");
            } else {
                System.out.println("\n--- Enrollments ---");
                System.out.printf("%-5s %-10s%n", "ID", "Student ID");
                System.out.println("-".repeat(20));
                for (Enrollment e : enrollments) {
                    System.out.printf("%-5d %-10d%n", e.getId(), e.getStudentId());
                }
            }
        } catch (Exception e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
    }

    // ==================== Grade Operations ====================
    private void handleGradeOperations() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Grade Management ---");
            System.out.println("1. Assign/Update Grade");
            System.out.println("2. View Grade by Enrollment");
            System.out.println("3. View All Grades");
            System.out.println("4. View Grades by Student");
            System.out.println("5. Delete Grade");
            System.out.println("0. Back to Main Menu");
            
            int choice = getIntInput("Enter your choice: ");
            
            switch (choice) {
                case 1 -> assignGrade();
                case 2 -> viewGradeByEnrollment();
                case 3 -> viewAllGrades();
                case 4 -> viewGradesByStudent();
                case 5 -> deleteGrade();
                case 0 -> back = true;
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void assignGrade() {
        System.out.println("\n--- Assign/Update Grade ---");
        System.out.println("Valid grades: A+, A, B+, B, C+, C, D+, D, F");
        
        int enrollmentId = getIntInput("Enter enrollment ID: ");
        String grade = getStringInput("Enter grade: ").toUpperCase();
        
        // Validate grade
        List<String> validGrades = List.of("A+", "A", "B+", "B", "C+", "C", "D+", "D", "F");
        if (!validGrades.contains(grade)) {
            System.out.println("✗ Invalid grade. Please use: A+, A, B+, B, C+, C, D+, D, F");
            return;
        }
        
        try {
            // Check if enrollment exists
            schoolService.getEnrollmentById(enrollmentId);
            
            // Check if grade already exists
            try {
                Grade existingGrade = schoolService.getByEnrollmentId(enrollmentId);
                // Update existing grade
                existingGrade.setGrade(grade);
                schoolService.updateGrade(existingGrade);
                System.out.println("✓ Grade updated successfully!");
            } catch (Exception e) {
                // Create new grade
                Grade newGrade = new Grade(enrollmentId, grade);
                schoolService.createGrade(newGrade);
                System.out.println("✓ Grade assigned successfully!");
            }
        } catch (Exception e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
    }

    private void viewGradeByEnrollment() {
        int enrollmentId = getIntInput("Enter enrollment ID: ");
        try {
            Grade grade = schoolService.getByEnrollmentId(enrollmentId);
            System.out.println("\n--- Grade Details ---");
            System.out.println("Grade ID: " + grade.getId());
            System.out.println("Enrollment ID: " + grade.getEnrollmentId());
            System.out.println("Grade: " + grade.getGrade());
        } catch (Exception e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
    }

    private void viewAllGrades() {
        System.out.println("\n--- All Grades ---");
        try {
            List<Grade> grades = schoolService.getAllEnrollments().stream()
                .map(e -> {
                    try {
                        return schoolService.getByEnrollmentId(e.getId());
                    } catch (Exception ex) {
                        return null;
                    }
                })
                .filter(g -> g != null)
                .toList();
            
            if (grades.isEmpty()) {
                System.out.println("No grades found.");
            } else {
                System.out.printf("%-5s %-15s %-10s%n", "ID", "Enrollment ID", "Grade");
                System.out.println("-".repeat(35));
                for (Grade g : grades) {
                    System.out.printf("%-5d %-15d %-10s%n", 
                        g.getId(), g.getEnrollmentId(), g.getGrade());
                }
            }
        } catch (Exception e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
    }

    private void viewGradesByStudent() {
        int studentId = getIntInput("Enter student ID: ");
        try {
            schoolService.getStudentById(studentId); // Verify student exists
            List<Grade> grades = schoolService.getGradesByStudentId(studentId);
            if (grades.isEmpty()) {
                System.out.println("No grades found for this student.");
            } else {
                System.out.println("\n--- Grades ---");
                System.out.printf("%-5s %-15s %-10s%n", "ID", "Enrollment ID", "Grade");
                System.out.println("-".repeat(35));
                for (Grade g : grades) {
                    System.out.printf("%-5d %-15d %-10s%n", 
                        g.getId(), g.getEnrollmentId(), g.getGrade());
                }
            }
        } catch (Exception e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
    }

    private void deleteGrade() {
        int gradeId = getIntInput("Enter grade ID to delete: ");
        try {
            Grade grade = schoolService.getGradeById(gradeId);
            System.out.println("Are you sure you want to delete grade: " + grade.getGrade() + "? (yes/no)");
            String confirm = scanner.nextLine().trim().toLowerCase();
            if (confirm.equals("yes")) {
                schoolService.deleteGrade(gradeId);
                System.out.println("✓ Grade deleted successfully!");
            } else {
                System.out.println("Deletion cancelled.");
            }
        } catch (Exception e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
    }

    // ==================== Report Operations ====================
    private void handleReportOperations() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Report Management ---");
            System.out.println("1. View Student Report");
            System.out.println("2. Export Student Report to File");
            System.out.println("0. Back to Main Menu");
            
            int choice = getIntInput("Enter your choice: ");
            
            switch (choice) {
                case 1 -> viewStudentReport();
                case 2 -> exportStudentReport();
                case 0 -> back = true;
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void viewStudentReport() {
        int studentId = getIntInput("Enter student ID: ");
        try {
            String report = schoolService.createAStudentReport(studentId);
            System.out.println("\n" + report);
        } catch (Exception e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
    }

    private void exportStudentReport() {
        int studentId = getIntInput("Enter student ID: ");
        try {
            schoolService.exportReportIntoFile(studentId);
            System.out.println("✓ Report exported successfully!");
        } catch (Exception e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
    }

    // ==================== Helper Methods ====================
    private int getIntInput(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                String input = scanner.nextLine().trim();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter a number: ");
            }
        }
    }

    private String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
}

