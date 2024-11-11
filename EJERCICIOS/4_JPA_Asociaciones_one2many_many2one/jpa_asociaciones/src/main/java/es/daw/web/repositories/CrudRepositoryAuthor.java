package es.daw.web.repositories;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import es.daw.web.entities.Author;
import es.daw.web.exceptions.JPAException;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@RequestScoped
public class CrudRepositoryAuthor implements CrudRepository<Author>,Serializable {

    @Inject
    private EntityManager em;

    @Override
    //@Transactional(readOnly=true)
    // Pasamos de usar .setHint("org.hibernate.readOnly", true) 
    @Transactional
    public Set<Author> select() throws JPAException {
        return new HashSet<>(em.createQuery("SELECT a FROM Author a order by a.id",Author.class).getResultList());
    }

    @Override
    @Transactional // no especificamos readOnly...
    public Optional<Author> selectById(int id) throws JPAException {
        return Optional.ofNullable(em.find(Author.class,id));
    }

    @Override
    public void deleteById(int id) throws JPAException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteById'");
    }

    @Override
    @Transactional
    public void save(Author t) throws JPAException {
        // ------------------------------------
        // BUG: SE DAN DE ALTA MISMO AUTORES
        // ------------------------------------
        // FORMA 1: VÍA COLECCIÓN JAVA
        // Set<Author> autores = select(); // si hay muchos registros se cargan todos en memoria... menos eficiente
        
        // if (!autores.contains(t))
        //     em.persist(t);

        // FORMA 2: VÍA SQL
        if (!existeAuthor(t))
            em.persist(t);
            // no necesito forzar con flush porque cada operación de persistencia se hace en un request diferente
            //em.flush();

        else{
            System.out.println("Ya existe el autor "+t.getName());
            throw new JPAException("Ya existe el autor "+t.getName());
        }
    }

    /*
     * 
     */
    private boolean existeAuthor(Author author){
        String jpql = "SELECT COUNT(a) FROM Author a where a.name = :nombre";
        // En JPA el select count devuelve un Long
        Long cantidad = em.createQuery(jpql, Long.class)
            .setParameter("nombre", author.getName())
            .getSingleResult();
            System.out.println("****** cantidad:"+cantidad);

        return cantidad > 0;
    }
    
}
