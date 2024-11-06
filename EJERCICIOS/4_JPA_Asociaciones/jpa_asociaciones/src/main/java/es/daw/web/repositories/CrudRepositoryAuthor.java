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
        try{
            em.persist(t);
            // no necesito forzar con flush porque cada operación de persistencia se hace en un request diferente
            //em.flush();
        }catch(Exception e){
            new JPAException(e.getMessage());
        }
    }
    
}
