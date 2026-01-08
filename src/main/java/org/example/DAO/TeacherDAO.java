package org.example.DAO;

import org.example.Datebase.DBConnection;
import org.example.Model.Student;
import org.example.Model.Teacher;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TeacherDAO implements DAO<Teacher> {

    @Override
    public Optional<Teacher> findById(int id) {
        String sql = "Select * From Teachers Where id = ?";
        try(Connection connection = DBConnection.getDataSource().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setInt(1,id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                Teacher teacher = new Teacher(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("specialization")
                );
                return Optional.of(teacher);
            }
        }catch (Exception e){
            System.out.println("Error While Fetching Teachers with Id"+e.getMessage());
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Teacher> findAll() {
        String sql = "Select * From Teachers";
        List<Teacher> teachers = new ArrayList<>();
        try(Connection connection = DBConnection.getDataSource().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                teachers.add(new Teacher(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("specialization")
                ));
            }
        }catch (Exception e){
            System.out.println("Error While Fetching Teachers"+e.getMessage());
            e.printStackTrace();
        }
        return teachers;
    }
    @Override
    public List<Teacher> findByName(String name){
        String sql = "Select * From Teachers Where name like Concat('%',?,'%')";
        List<Teacher> teachers = new ArrayList<>();
        try(Connection connection = DBConnection.getDataSource().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setString(1,name);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                teachers.add(new Teacher(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("specialization")
                ));
            }
        }catch (Exception e){
            System.out.println("Error While Fetching Teachers with Name"+e.getMessage());
            e.printStackTrace();
        }
        return teachers;
    }
    @Override
    public boolean update(Teacher teacher) {
        String sql = "Update Teachers Set name = ? , specialization = ?  Where id = ?";
        try(Connection connection = DBConnection.getDataSource().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setString(1,teacher.getName());
            preparedStatement.setString(2,teacher.getSpecialization());
            preparedStatement.setInt(3,teacher.getId());
            int res = preparedStatement.executeUpdate() ;
            System.out.println("Teacher Updated");
            return res > 0;
        }catch (Exception e){
            System.out.println("Error While Updating Teacher "+e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(Teacher teacher) {
        String sql = "Delete From Teachers Where id = ?";
        try(Connection connection = DBConnection.getDataSource().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setInt(1,teacher.getId());
            int res = preparedStatement.executeUpdate();
            System.out.println("Teacher Deleted");
            return res > 0;
        }catch (Exception e){
            System.out.println("Error While Deleting Teacher "+e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean save(Teacher teacher) {
        String sql = "Insert Into Students(name,specialization) Values (?,?)";
        try(Connection connection = DBConnection.getDataSource().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1,teacher.getName());
            preparedStatement.setString(2,teacher.getSpecialization());
            int res = preparedStatement.executeUpdate();

            System.out.println("Teacher Saved");
            return res > 0;
        }catch (Exception e){
            System.out.println("Error While Saving Teacher "+e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
}
