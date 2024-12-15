package es.daw.web.repositories;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import es.daw.web.entities.Categoria;
import es.daw.web.exceptions.JPAException;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

@RequestScoped
public class CrudRepositoryCategoria implements CrudRepository<Categoria>{

    @Inject
    EntityManager em;
    
    @Override
    public Set<Categoria> select() throws JPAException {
        throw new UnsupportedOperationException("Unimplemented method 'select'");
    }

    @Override
    public Optional<Categoria> selectById(int id) throws JPAException {
        try{
            return Optional.ofNullable(em.find(Categoria.class,id));
        }catch(Exception e){
            new JPAException(JpaManagerCdi.getMessageError(e));
        }
        return Optional.empty();
    }

    @Override
    public void deleteById(int id) throws JPAException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteById'");
    }

    @Override
    public void save(Categoria t) throws JPAException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'save'");
    }

    @Override
    public Set<Categoria> selectAll() throws JPAException {

        try{
            String jpql = "SELECT c FROM Categoria c";

            List<Categoria> lista = em.createQuery(jpql,Categoria.class)
            .getResultList();

            Set<Categoria> categorias = new LinkedHashSet<>(lista);

            return categorias;
            
        }catch(Exception e){
            throw new JPAException(JpaManagerCdi.getMessageError(e));
        }
    }
    
    
}
