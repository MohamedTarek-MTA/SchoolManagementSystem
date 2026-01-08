package org.example.Service;

import lombok.RequiredArgsConstructor;
import org.example.DAO.*;
import org.example.Model.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;

@RequiredArgsConstructor
public class SchoolService {
    private final TeacherDAO teacherDAO;
    private final StudentDAO studentDAO;
    private final SubjectDAO subjectDAO;
    private final GradeDAO gradeDAO;
    private final EnrollmentDAO enrollmentDAO;

    public Teacher getByTeacherId(int teacherId){
        return teacherDAO.findById(teacherId).orElseThrow(()->new IllegalArgumentException("Teacher Not Found"));
    }
    public List<Teacher> getAll(){
        return teacherDAO.findAll();
    }
    public void updateTeacher(Teacher teacher){
        teacherDAO.update(teacher);
    }
    public void createTeacher(Teacher teacher){
        teacherDAO.save(teacher);
    }
    public void deleteTeacher(int teacherId){
        teacherDAO.delete(getByTeacherId(teacherId));
    }
    public List<Teacher> getByTeacherName(String name){
       return teacherDAO.findByName(name);
    }
    public void createSubject(Subject subject){
        subjectDAO.save(subject);
    }
    public void updateSubject(Subject subject){
        subjectDAO.update(subject);
    }
    public Subject getSubjectById(int subjectId){
        return subjectDAO.findById(subjectId).orElseThrow(()->new IllegalArgumentException("Subject Not Found"));
    }
    public List<Subject> getSubjectByTeacherId(int teacherId){
        return subjectDAO.findByTeacherId(teacherId);
    }
    public void deleteSubjectById(int subjectId){
        subjectDAO.delete(getSubjectById(subjectId));
    }
    public List<Enrollment> getAllEnrollments(){
        return enrollmentDAO.findAll();
    }
    public Enrollment getEnrollmentById(int enrollmentId){
        return enrollmentDAO.findById(enrollmentId).orElseThrow(()->new IllegalArgumentException("Enrollment Not Found"));
    }
    public List<Enrollment> getEnrollmentsByStudentId(int studentId){
        return enrollmentDAO.findByStudentId(studentId);
    }
    public List<Enrollment> getEnrollmentsBySubjectId(int subjectId){
        return enrollmentDAO.findBySubjectId(subjectId);
    }
    public void createGrade(Grade grade){
        gradeDAO.save(grade);
    }
    public void updateGrade(Grade grade){
        gradeDAO.update(grade);
    }
    public Grade getGradeById(int gradeId){
        return gradeDAO.findById(gradeId).orElseThrow(()->new IllegalArgumentException("Grade Not Found"));
    }
    public Grade getByEnrollmentId(int enrollmentId){
        return gradeDAO.findByEnrollmentId(enrollmentId).orElseThrow(()->new IllegalArgumentException("Grade Not Found"));
    }
    public void deleteGrade(int gradeId){
        gradeDAO.delete(getGradeById(gradeId));
    }
    ///////////////////////////////////////////////////////////////////////////////////////////

    public Student getStudentById(int id){
        return studentDAO.findById(id).orElseThrow(()->new IllegalArgumentException("Student Not Found"));
    }
    public List<Student> getAllStudents(){
        return studentDAO.findAll();
    }

    public List<Student> getStudentByName(String name){
        return studentDAO.findByName(name);
    }
    public Student getByEmail(String email){
        return studentDAO.findByEmail(email).orElseThrow(()->new IllegalArgumentException("Student Not Found"));
    }
    public void createStudent(Student student){
        studentDAO.save(student);
    }
    public void updateStudent(Student student){
        studentDAO.update(student);
    }

    public void deleteStudent(int id){
        studentDAO.delete(getStudentById(id));
    }
    public void enrollStudentToSubject(int studentId,int subjectId){
        enrollmentDAO.save(new Enrollment(studentId,subjectId));
    }
    public List<Subject> getAllSubjects(){
        return subjectDAO.findAll();
    }
    public List<Grade> getGradesByStudentId(int studentId){
        var enrollments = enrollmentDAO.findByStudentId(studentId);
        List<Grade> grades = new ArrayList<>();
        for(Enrollment enrollment : enrollments){
            gradeDAO.findByEnrollmentId(enrollment.getId()).ifPresent(grades::add);
        }
        return grades;
    }

    public List<Subject> getSubjectsByStudentId(int studentId){
        var enrollments = enrollmentDAO.findByStudentId(studentId);
        List<Subject> subjects = new ArrayList<>();
        for(Enrollment enrollment : enrollments){
            subjectDAO.findById(enrollment.getSubjectId()).ifPresent(subjects::add);
        }
        return subjects;
    }
    public double calculateTotalGPA(List<Grade> grades){
        if (grades == null || grades.isEmpty()) {
            return 0.0;
        }

        double totalPoints = 0.0;

        for (Grade grade : grades) {
            totalPoints += mapGradeToPoint(grade.getGrade());
        }

        // GPA = average
        return totalPoints / grades.size();
    }
    private double mapGradeToPoint(String grade) {

        if (grade == null) return 0.0;

        return switch (grade.toLowerCase()) {
            case "a+", "a" -> 4.0;
            case "b+" -> 3.5;
            case "b" -> 3.0;
            case "c+" -> 2.5;
            case "c" -> 2.0;
            case "d+" -> 1.5;
            case "d" -> 1.0;
            default -> 0.0;
        };
    }

    public String createAStudentReport(int studentId) {


        Student student = getStudentById(studentId);


        List<Subject> subjects = getSubjectsByStudentId(studentId);
        List<Grade> grades = getGradesByStudentId(studentId);


        double totalGPA = calculateTotalGPA(grades);


        StringBuilder report = new StringBuilder();

        report.append("===== Student Report =====\n");
        report.append("ID: ").append(student.getId()).append("\n");
        report.append("Name: ").append(student.getName()).append("\n");
        report.append("Email: ").append(student.getEmail()).append("\n");
        report.append("Age: ").append(student.getAge()).append("\n\n");

        report.append("Subjects & Grades:\n");


        Map<Integer, String> enrollmentGradeMap = new HashMap<>();
        for (Grade grade : grades) {
            enrollmentGradeMap.put(grade.getEnrollmentId(), grade.getGrade());
        }


        var enrollments = enrollmentDAO.findByStudentId(studentId);

        for (Enrollment enrollment : enrollments) {
            int subjectId = enrollment.getSubjectId();
            Optional<Subject> subjectOpt = subjectDAO.findById(subjectId);
            if (subjectOpt.isPresent()) {
                Subject subject = subjectOpt.get();
                String gradeStr = enrollmentGradeMap.getOrDefault(enrollment.getId(), "N/A");
                report.append(" - ").append(subject.getName())
                        .append(": ").append(gradeStr).append("\n");
            }
        }

        report.append("\nTotal GPA: ").append(String.format("%.2f", totalGPA)).append("\n");
        report.append("===========================\n");

        return report.toString();
    }
    public void exportReportIntoFile(int studentId){
        String report = createAStudentReport(studentId);
        try{
            Path path = Path.of("C:\\Reports","Student_"+studentId+"_report");
            Files.createDirectories(path.getParent());
            Files.writeString(
                    path,
                    report,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
            );
            System.out.println("Report saved at: " + path.toAbsolutePath());
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
