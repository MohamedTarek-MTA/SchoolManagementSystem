package org.example.DAO;

import org.example.Model.Student;
import org.example.Model.Teacher;

import java.util.List;
import java.util.Optional;

public class TeacherDAO implements DAO<Teacher> {

    @Override
    public Optional<Teacher> findById(int id) {
        return Optional.empty();
    }

    @Override
    public List<Teacher> findAll() {
        return null;
    }
    @Override
    public List<Teacher> findByName(String name){
        return null;
    }
    @Override
    public boolean update(Teacher teacher) {
        return false;
    }

    @Override
    public boolean delete(Teacher teacher) {
        return false;
    }

    @Override
    public boolean save(Teacher teacher) {
        return false;
    }
}
