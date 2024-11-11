package es.daw.web.repositories;

import java.util.Optional;
import java.util.Set;

import es.daw.web.exceptions.JPAException;

public interface CrudRepository<T> {
    Set<T> select() throws JPAException;
    Optional<T> selectById(int id) throws JPAException;
    default Optional<T> selectByPropiedad(T t) throws JPAException{return Optional.empty();}
    void deleteById(int id) throws JPAException;
    default void delete(T t) throws JPAException{};
    void save(T t) throws JPAException;
}