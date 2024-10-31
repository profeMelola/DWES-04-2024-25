package es.daw.web.repositories;

import java.util.*;

import es.daw.web.exceptions.JPAException;

public interface CrudRepository<T> {
    
    List<T> select();
    Optional<T> selectById(int id);
    void deleteById(int id) throws JPAException;
    // void insert(T t);
    // void update(T t);
    void save(T t); // si existe el fabricante, hace update, si no existe, hace insert

}
