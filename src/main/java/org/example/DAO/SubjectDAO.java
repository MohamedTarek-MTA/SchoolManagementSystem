package org.example.DAO;

import org.example.Datebase.DBConnection;
import org.example.Model.Subject;
import org.example.Model.Teacher;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SubjectDAO implements DAO<Subject> {
    @Override
    public Optional<Subject> findById(int id) {
        String sql = "Select * From Subjects Where id = ?";
        try(Connection connection = DBConnection.getDataSource().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setInt(1,id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                Subject subject = new Subject(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getInt("teacher_id")
                );
                return Optional.of(subject);
            }
        }catch (Exception e){
            System.out.println("Error While Fetching Subject with Id"+e.getMessage());
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Subject> findAll() {
        String sql = "Select * From Subjects";
        List<Subject> subjects = new ArrayList<>();
        try(Connection connection = DBConnection.getDataSource().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                subjects.add(new Subject(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getInt("teacher_id")
                ));
            }
        }catch (Exception e){
            System.out.println("Error While Fetching Subject"+e.getMessage());
            e.printStackTrace();
        }
        return subjects;
    }

    @Override
    public boolean update(Subject subject) {
        String sql = "Update Subjects Set name = ? , teacher_id = ?  Where id = ?";
        try(Connection connection = DBConnection.getDataSource().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setString(1,subject.getName());
            preparedStatement.setInt(2,subject.getTeacherId());
            preparedStatement.setInt(3,subject.getId());
            int res = preparedStatement.executeUpdate() ;
            System.out.println("Subject Updated");
            return res > 0;
        }catch (Exception e){
            System.out.println("Error While Updating Subject "+e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(Subject subject) {
        String sql = "Delete From Subjects Where id = ?";
        try(Connection connection = DBConnection.getDataSource().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setInt(1,subject.getId());
            int res = preparedStatement.executeUpdate();
            System.out.println("Subject Deleted");
            return res > 0;
        }catch (Exception e){
            System.out.println("Error While Deleting Subject "+e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean save(Subject subject) {
        String sql = "Insert Into Subjects(name,teacher_id) Values (?,?)";
        try(Connection connection = DBConnection.getDataSource().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1,subject.getName());
            preparedStatement.setInt(2,subject.getTeacherId());
            int res = preparedStatement.executeUpdate();

            System.out.println("Subject Saved");
            return res > 0;
        }catch (Exception e){
            System.out.println("Error While Saving Subject "+e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
}
