package es.daw.web.repositories;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import es.daw.web.entities.Book;
import es.daw.web.exceptions.JPAException;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@RequestScoped
public class CrudRepositoryBook implements CrudRepository<Book>,Serializable {

    @Inject
    private EntityManager em;

    @Override
    //@Transactional(readOnly=true)
    // Pasamos de usar .setHint("org.hibernate.readOnly", true) 
    @Transactional
    public Set<Book> select() throws JPAException {
        // PRUEBAS POR EL TEMA DEL ORDEN...
        List<Book> lista = em.createQuery("SELECT b FROM Book b order by b.id desc",Book.class).getResultList();

        System.out.println("************ LISTA DE LIBROS ******************");
        lista.forEach(System.out::println);
        System.out.println("***********************************************");

        Set<Book> conjunto = new HashSet<>(lista);
        System.out.println("************ CONJUNTO DE LIBROS ******************");
        conjunto.forEach(System.out::println);
        System.out.println("***********************************************");

        Set<Book> conjuntoOrdenado = new LinkedHashSet<>(lista);
        System.out.println("************ CONJUNTO DE LIBROS ******************");
        conjuntoOrdenado.forEach(System.out::println);
        System.out.println("***********************************************");

        return conjuntoOrdenado; // al convertirlo a HashSet no se garantiza el orden. Lo convierto a un LinkedHashSet que conserva el orden de insercción

        //return new HashSet<>(em.createQuery("SELECT b FROM Book b order by b.id desc",Book.class).getResultList());
    }

    @Override
    @Transactional // no especificamos readOnly...
    public Optional<Book> selectById(int id) throws JPAException {
        return Optional.ofNullable(em.find(Book.class,id));

    }

    @Override
    public void deleteById(int id) throws JPAException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteById'");
    }

    @Override
    public void save(Book t) throws JPAException {
        //em.persist(t);

        // no necesito forzar con flush porque cada operación de persistencia se hace en un request diferente
        //em.flush(); 
    }
    
}
