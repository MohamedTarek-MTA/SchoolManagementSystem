package org.example.DAO;

import org.example.Datebase.DBConnection;
import org.example.Model.Enrollment;
import org.example.Model.Grade;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GradeDAO implements DAO<Grade>{
    @Override
    public Optional<Grade> findById(int id) {
        String sql = "Select * From Grades Where id = ?";
        try(Connection connection = DBConnection.getDataSource().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setInt(1,id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                Grade grade = new Grade(
                        resultSet.getInt("id"),
                        resultSet.getInt("enrollment_id"),
                        resultSet.getString("grade")
                );
                return Optional.of(grade);
            }
        }catch (Exception e){
            System.out.println("Error While Fetching Grade with Id"+e.getMessage());
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Grade> findAll() {
        String sql = "Select * From Grades";
        List<Grade> grades = new ArrayList<>();
        try(Connection connection = DBConnection.getDataSource().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                grades.add(new Grade(
                        resultSet.getInt("id"),
                        resultSet.getInt("enrollment_id"),
                        resultSet.getString("grade")
                ));
            }
        }catch (Exception e){
            System.out.println("Error While Fetching Grade "+e.getMessage());
            e.printStackTrace();
        }
        return grades;
    }

    @Override
    public boolean update(Grade grade) {
        String sql = "Update Grades Set enrollment_id = ? , grade = ?  Where id = ?";
        try(Connection connection = DBConnection.getDataSource().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setInt(1,grade.getEnrollmentId());
            preparedStatement.setString(2,grade.getGrade());
            preparedStatement.setInt(3,grade.getId());
            int res = preparedStatement.executeUpdate() ;
            System.out.println("Grade Updated");
            return res > 0;
        }catch (Exception e){
            System.out.println("Error While Updating Grade "+e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(Grade grade) {
        String sql = "Delete From Grades Where id = ?";
        try(Connection connection = DBConnection.getDataSource().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setInt(1,grade.getId());
            int res = preparedStatement.executeUpdate();
            System.out.println("Grade Deleted");
            return res > 0;
        }catch (Exception e){
            System.out.println("Error While Deleting Grade "+e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean save(Grade grade) {
        String sql = "Insert Into Grades(enrollment_id,grade) Values (?,?)";
        try(Connection connection = DBConnection.getDataSource().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1,grade.getEnrollmentId());
            preparedStatement.setString(2,grade.getGrade());
            int res = preparedStatement.executeUpdate();

            System.out.println("Grade Saved");
            return res > 0;
        }catch (Exception e){
            System.out.println("Error While Saving Grade "+e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
}
