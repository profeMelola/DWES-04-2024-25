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
            // BUG: SE DAN DE ALTA EL MISMO AUTOR CON SUS LIBROS (REPETIDOS)

            // FORMA 1: vía Java (menos eficiente, rendimiento)
            // Set<Author> autores = select(); 

            // if (!autores.contains(t))
            //     em.persist(t);
            

            // FORMA 2: vía JPA (bd)
            System.out.println("********+ save!!!!");
            if (!existeAuthor(t)){
                System.out.println("******* salvo !!!");
                em.persist(t);
            }else{
                System.out.println("******* ya existe!!!!");
                throw new JPAException("Ya existe el autor "+t.getName());
            }


            //em.persist(t);
            // no necesito forzar con flush porque cada operación de persistencia se hace en un request diferente
            //em.flush();
        }catch(Exception e){
            e.printStackTrace();
            throw new JPAException(e.getMessage());
        }
    }

    /**
     * 
     * @param autor
     * @return
     */
    private boolean existeAuthor(Author autor){
        String jpql = "SELECT COUNT(a) FROM Author a where a.name = :nombre";
        Long cantidad = em.createQuery(jpql,Long.class)
            .setParameter("nombre",autor.getName())
            .getSingleResult();

        System.out.println("***** cantidad:"+cantidad);

        return cantidad > 0;

        
    }
    
}
