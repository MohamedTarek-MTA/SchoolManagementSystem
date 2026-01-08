package org.example.DAO;

import lombok.RequiredArgsConstructor;
import org.example.Datebase.DBConnection;
import org.example.Model.Student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@RequiredArgsConstructor
public class StudentDAO implements DAO<Student>{
    @Override
    public Optional<Student> findById(int id) {
        String sql = "Select * From Students Where id = ?";
        try(Connection connection = DBConnection.getDataSource().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setInt(1,id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                Student student = new Student(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getInt("age")
                );
                return Optional.of(student);
            }
        }catch (Exception e){
            System.out.println("Error While Fetching Student with Id"+e.getMessage());
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<Student> findByEmail(String email) {
        String sql = "Select * From Students Where email = ?";
        try(Connection connection = DBConnection.getDataSource().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setString(1,email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                Student student = new Student(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getInt("age")
                );
                return Optional.of(student);
            }
        }catch (Exception e){
            System.out.println("Error While Fetching Student with Email"+e.getMessage());
            e.printStackTrace();
        }
        return Optional.empty();
    }
    @Override
    public List<Student> findByName(String name){
        String sql = "Select * From Students Where name like Concat('%',?,'%')";
        List<Student> students = new ArrayList<>();
        try(Connection connection = DBConnection.getDataSource().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setString(1,name);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                students.add(new Student(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getInt("age")
                ));
            }
        }catch (Exception e){
            System.out.println("Error While Fetching Student with Name"+e.getMessage());
            e.printStackTrace();
        }
        return students;
    }
    @Override
    public List<Student> findAll() {
        String sql = "Select * From Students";
        List<Student> students = new ArrayList<>();
        try(Connection connection = DBConnection.getDataSource().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                students.add(new Student(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getInt("age")
                ));
            }
        }catch (Exception e){
            System.out.println("Error While Fetching Student with Name"+e.getMessage());
            e.printStackTrace();
        }
        return students;
    }

    @Override
    public boolean update(Student student) {
        String sql = "Update Students Set name = ? , email = ? , age = ? Where id = ?";
        try(Connection connection = DBConnection.getDataSource().getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setString(1,student.getName());
            preparedStatement.setString(2,student.getEmail());
            preparedStatement.setInt(3,student.getAge());
            preparedStatement.setInt(4,student.getId());
            int res = preparedStatement.executeUpdate() ;
            System.out.println("Student Updated");
            return res > 0;
        }catch (Exception e){
            System.out.println("Error While Updating Student "+e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(Student student) {
        String sql = "Delete From Students Where id = ?";
        try(Connection connection = DBConnection.getDataSource().getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setInt(1,student.getId());
            int res = preparedStatement.executeUpdate();
            System.out.println("Student Deleted");
            return res > 0;
        }catch (Exception e){
            System.out.println("Error While Deleting Student "+e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean save(Student student) {
        String sql = "Insert Into Students(name,email,age) Values (?,?,?)";
        try(Connection connection = DBConnection.getDataSource().getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1,student.getName());
            preparedStatement.setString(2,student.getEmail());
            preparedStatement.setInt(3,student.getAge());
            int res = preparedStatement.executeUpdate();

            System.out.println("Student Saved");
            return res > 0;
        }catch (Exception e){
            System.out.println("Error While Deleting Student "+e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
}
