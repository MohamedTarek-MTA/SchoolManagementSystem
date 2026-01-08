//package org.example.Service;
//
//import lombok.RequiredArgsConstructor;
//import org.example.DAO.EnrollmentDAO;
//import org.example.DAO.GradeDAO;
//import org.example.DAO.StudentDAO;
//import org.example.DAO.SubjectDAO;
//import org.example.Model.Enrollment;
//import org.example.Model.Grade;
//import org.example.Model.Student;
//import org.example.Model.Subject;
//
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.StandardOpenOption;
//import java.util.*;
//
//@RequiredArgsConstructor
//public class StudentService {
//    private final StudentDAO studentDAO;
//    private final GradeDAO gradeDAO;
//    private final EnrollmentDAO enrollmentDAO;
//    private final SubjectDAO subjectDAO;
//
//    public Student getStudentById(int id){
//        return studentDAO.findById(id).orElseThrow(()->new IllegalArgumentException("Student Not Found"));
//    }
//    public List<Student> getAllStudents(){
//        return studentDAO.findAll();
//    }
//
//    public List<Student> getStudentByName(String name){
//        return studentDAO.findByName(name);
//    }
//    public Student getByEmail(String email){
//        return studentDAO.findByEmail(email).orElseThrow(()->new IllegalArgumentException("Student Not Found"));
//    }
//    public void createStudent(Student student){
//        studentDAO.save(student);
//    }
//    public void updateStudent(Student student){
//        studentDAO.update(student);
//    }
//
//    public void deleteStudent(int id){
//        studentDAO.delete(getStudentById(id));
//    }
//    public void enrollStudentToSubject(int studentId,int subjectId){
//        enrollmentDAO.save(new Enrollment(studentId,subjectId));
//    }
//    public List<Subject> getAllSubjects(){
//        return subjectDAO.findAll();
//    }
//    public List<Grade> getGradesByStudentId(int studentId){
//        var enrollments = enrollmentDAO.findByStudentId(studentId);
//        List<Grade> grades = new ArrayList<>();
//        for(Enrollment enrollment : enrollments){
//            gradeDAO.findByEnrollmentId(enrollment.getId()).ifPresent(grades::add);
//        }
//        return grades;
//    }
//
//    public List<Subject> getSubjectsByStudentId(int studentId){
//        var enrollments = enrollmentDAO.findByStudentId(studentId);
//        List<Subject> subjects = new ArrayList<>();
//        for(Enrollment enrollment : enrollments){
//            subjectDAO.findById(enrollment.getSubjectId()).ifPresent(subjects::add);
//        }
//        return subjects;
//    }
//    public double calculateTotalGPA(List<Grade> grades){
//        if (grades == null || grades.isEmpty()) {
//            return 0.0;
//        }
//
//        double totalPoints = 0.0;
//
//        for (Grade grade : grades) {
//            totalPoints += mapGradeToPoint(grade.getGrade());
//        }
//
//        // GPA = average
//        return totalPoints / grades.size();
//    }
//    private double mapGradeToPoint(String grade) {
//
//        if (grade == null) return 0.0;
//
//        return switch (grade.toLowerCase()) {
//            case "a+", "a" -> 4.0;
//            case "b+" -> 3.5;
//            case "b" -> 3.0;
//            case "c+" -> 2.5;
//            case "c" -> 2.0;
//            case "d+" -> 1.5;
//            case "d" -> 1.0;
//            default -> 0.0;
//        };
//    }
//
//    public String createAStudentReport(int studentId) {
//
//
//        Student student = getStudentById(studentId);
//
//
//        List<Subject> subjects = getSubjectsByStudentId(studentId);
//        List<Grade> grades = getGradesByStudentId(studentId);
//
//
//        double totalGPA = calculateTotalGPA(grades);
//
//
//        StringBuilder report = new StringBuilder();
//
//        report.append("===== Student Report =====\n");
//        report.append("ID: ").append(student.getId()).append("\n");
//        report.append("Name: ").append(student.getName()).append("\n");
//        report.append("Email: ").append(student.getEmail()).append("\n");
//        report.append("Age: ").append(student.getAge()).append("\n\n");
//
//        report.append("Subjects & Grades:\n");
//
//
//        Map<Integer, String> enrollmentGradeMap = new HashMap<>();
//        for (Grade grade : grades) {
//            enrollmentGradeMap.put(grade.getEnrollmentId(), grade.getGrade());
//        }
//
//
//        var enrollments = enrollmentDAO.findByStudentId(studentId);
//
//        for (Enrollment enrollment : enrollments) {
//            int subjectId = enrollment.getSubjectId();
//            Optional<Subject> subjectOpt = subjectDAO.findById(subjectId);
//            if (subjectOpt.isPresent()) {
//                Subject subject = subjectOpt.get();
//                String gradeStr = enrollmentGradeMap.getOrDefault(enrollment.getId(), "N/A");
//                report.append(" - ").append(subject.getName())
//                        .append(": ").append(gradeStr).append("\n");
//            }
//        }
//
//        report.append("\nTotal GPA: ").append(String.format("%.2f", totalGPA)).append("\n");
//        report.append("===========================\n");
//
//        return report.toString();
//    }
//    public void exportReportIntoFile(int studentId){
//        String report = createAStudentReport(studentId);
//        try{
//            Path path = Path.of("C:\\Reports","Student_"+studentId+"_report");
//            Files.createDirectories(path.getParent());
//            Files.writeString(
//                    path,
//                    report,
//                    StandardOpenOption.CREATE,
//                    StandardOpenOption.TRUNCATE_EXISTING
//            );
//            System.out.println("Report saved at: " + path.toAbsolutePath());
//        }catch (IOException e){
//            e.printStackTrace();
//        }
//    }
//}
