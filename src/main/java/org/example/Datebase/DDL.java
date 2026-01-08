package org.example.Datebase;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DDL{
    public static void createDatabase(){
        try(Connection connection = DBConnection.getBootstrapDataSource().getConnection();
            Statement statement = connection.createStatement()){
            statement.executeUpdate("Create Database If Not Exists SchoolDB");

            System.out.println("Database is ready.");
        }catch(Exception e){
            System.out.println("Database not created"+e.getMessage());
            e.printStackTrace();
        }
    }
    public static void createTables(){
        try(Connection connection = DBConnection.getAppDataSource().getConnection();
        Statement statement = connection.createStatement()){
            statement.executeUpdate("Create Table If Not Exists Students(id Int primary key auto_increment,`name` varchar(100) not null, email varchar(100) unique not null,age int not null)");
            statement.executeUpdate("Create Table If Not Exists Teachers(id Int primary key auto_increment,`name` varchar(100) not null, specialization varchar(100))");
            statement.executeUpdate("Create Table If Not Exists Subjects(id Int primary key auto_increment,`name` varchar(100) not null, teacher_id int, Foreign key (teacher_id) References Teachers(id) )");
            statement.executeUpdate("Create Table If Not Exists Enrollments(id Int primary key auto_increment,student_id int,subject_id int, Foreign Key(student_id) References Students(id),Foreign Key(subject_id) References Subjects(id) )");
            statement.executeUpdate("Create Table If Not Exists Grades(id Int primary key auto_increment, grade varchar(2), enrollment_id int,Foreign Key(enrollment_id) References Enrollments(id) )");
            System.out.println("Tables Created Successfully");

        }catch (Exception e){
            System.out.println("Table not created"+e.getMessage());
            e.printStackTrace();
        }
    }
}
