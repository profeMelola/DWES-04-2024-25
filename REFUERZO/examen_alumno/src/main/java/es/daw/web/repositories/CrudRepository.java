package es.daw.web.repositories;

import java.util.Optional;
import java.util.Set;

import es.daw.web.exceptions.JPAException;

public interface CrudRepository<T> {

    public abstract Set<T> select() throws JPAException;
    public abstract Optional<T> selectById(int id) throws JPAException;
    public default Optional<T> selectByPropiedad(T t) throws JPAException{
        return Optional.empty();
    }
    public abstract void deleteById(int id) throws JPAException;
    public default void delete(T t) throws JPAException{}
    public abstract void save(T t) throws JPAException;
    
}