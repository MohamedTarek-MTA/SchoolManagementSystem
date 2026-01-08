package org.example.DAO;

import org.example.Datebase.DBConnection;
import org.example.Model.Enrollment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EnrollmentDAO implements DAO<Enrollment>{
    @Override
    public Optional<Enrollment> findById(int id) {
        String sql = "Select * From Enrollments Where id = ?";
        try(Connection connection = DBConnection.getAppDataSource().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setInt(1,id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                Enrollment enrollment = new Enrollment(
                        resultSet.getInt("id"),
                        resultSet.getInt("student_id"),
                        resultSet.getInt("subject_id")
                );
                return Optional.of(enrollment);
            }
        }catch (Exception e){
            System.out.println("Error While Fetching Enrollment with Id"+e.getMessage());
            e.printStackTrace();
        }
        return Optional.empty();
    }
    public List<Enrollment> findByStudentId(int student_id) {
        String sql = "Select * From Enrollments Where student_id = ?";
        List<Enrollment> enrollments = new ArrayList<>();
        try(Connection connection = DBConnection.getAppDataSource().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setInt(1,student_id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                enrollments.add( new Enrollment(
                        resultSet.getInt("id"),
                        resultSet.getInt("student_id"),
                        resultSet.getInt("subject_id")
                ));
            }
        }catch (Exception e){
            System.out.println("Error While Fetching Enrollment with Id"+e.getMessage());
            e.printStackTrace();
        }
        return enrollments;
    }
    public List<Enrollment> findBySubjectId(int subject_id) {
        String sql = "Select * From Enrollments Where subject_id = ?";
        List<Enrollment> enrollments = new ArrayList<>();
        try(Connection connection = DBConnection.getAppDataSource().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setInt(1,subject_id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                enrollments.add( new Enrollment(
                        resultSet.getInt("id"),
                        resultSet.getInt("student_id"),
                        resultSet.getInt("subject_id")
                ));
            }
        }catch (Exception e){
            System.out.println("Error While Fetching Enrollment with Id"+e.getMessage());
            e.printStackTrace();
        }
        return enrollments;
    }
    @Override
    public List<Enrollment> findAll() {
        String sql = "Select * From Enrollments";
        List<Enrollment> enrollments = new ArrayList<>();
        try(Connection connection = DBConnection.getAppDataSource().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                enrollments.add(new Enrollment(
                        resultSet.getInt("id"),
                        resultSet.getInt("student_id"),
                        resultSet.getInt("subject_id")
                ));
            }
        }catch (Exception e){
            System.out.println("Error While Fetching Enrollment "+e.getMessage());
            e.printStackTrace();
        }
        return enrollments;
    }

    @Override
    public boolean update(Enrollment enrollment) {
        String sql = "Update Enrollments Set student_id = ? , subject_id = ?  Where id = ?";
        try(Connection connection = DBConnection.getAppDataSource().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setInt(1,enrollment.getStudentId());
            preparedStatement.setInt(2,enrollment.getSubjectId());
            preparedStatement.setInt(3,enrollment.getId());
            int res = preparedStatement.executeUpdate() ;
            System.out.println("Enrollment Updated");
            return res > 0;
        }catch (Exception e){
            System.out.println("Error While Updating Enrollment "+e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(Enrollment enrollment) {
        String sql = "Delete From Enrollments Where id = ?";
        try(Connection connection = DBConnection.getAppDataSource().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setInt(1,enrollment.getId());
            int res = preparedStatement.executeUpdate();
            System.out.println("Enrollment Deleted");
            return res > 0;
        }catch (Exception e){
            System.out.println("Error While Deleting Enrollment "+e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean save(Enrollment enrollment) {
        String sql = "Insert Into Enrollments(student_id,subject_id) Values (?,?)";
        try(Connection connection = DBConnection.getAppDataSource().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1,enrollment.getStudentId());
            preparedStatement.setInt(2,enrollment.getSubjectId());
            int res = preparedStatement.executeUpdate();

            System.out.println("Enrollment Saved");
            return res > 0;
        }catch (Exception e){
            System.out.println("Error While Saving Enrollment "+e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
}
