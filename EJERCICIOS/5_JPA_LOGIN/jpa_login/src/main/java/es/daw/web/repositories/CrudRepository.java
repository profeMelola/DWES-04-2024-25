package es.daw.web.repositories;

import java.util.Optional;
import java.util.Set;

import es.daw.web.exceptions.JPAException;

public interface CrudRepository<T> {
    
    Set<T> select() throws JPAException;
    Optional<T> selectById(int id) throws JPAException;
    //Optional<T> selectByPropiedad(T t) throws JPAException;
    void deleteById(int id) throws JPAException;
    void save(T t) throws JPAException;

}