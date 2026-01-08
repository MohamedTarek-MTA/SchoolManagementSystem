package org.example.DAO;

import java.util.List;
import java.util.Optional;

public interface DAO<T>{
    Optional<T> findById(int id);
   default Optional<T> findByEmail(String email){
       return Optional.empty();
   }
   default List<T> findByName(String name){
       return null;
   }
    List<T> findAll();
    boolean update(T t);
    boolean delete(T t);
    boolean save(T t);
}
