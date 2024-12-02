package es.daw.web.repositories;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import es.daw.web.entities.Producto;
import es.daw.web.exceptions.JPAException;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.Model;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@Model
public class CrudRepositoryProducto implements CrudRepository<Producto>{

    @Inject
    EntityManager em;
    
    @Override
    //@Transactional // debería ser transactional read only
    public Set<Producto> select() throws JPAException {
        try{
            String jpql = "SELECT p FROM Producto p where p.precio > :precio order by p.precio desc";
        
            List<Producto> lista = em.createQuery(jpql,Producto.class)
            .setParameter("precio", 35)
            .getResultList();
    
            Set<Producto> productos = new LinkedHashSet<>(lista);

            System.out.println("************ CRUD SELECT **********");
            productos.forEach(System.out::println);
            System.out.println("***********************************");            
    
            return productos;
    
        }catch(Exception e){
            System.err.println("************** ERROR AL LISTAR ************");
            //e.printStackTrace();
            throw new JPAException(JpaManagerCdi.getMessageError(e));
        }

    }

    @Override
    public Optional<Producto> selectById(int id) throws JPAException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'selectById'");
    }

    @Override
    public void deleteById(int id) throws JPAException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteById'");
    }

    @Override
    @Transactional
    public void save(Producto t) throws JPAException {
        try{
            em.persist(t);
            em.flush();
        }catch(Exception e){
            throw new JPAException(JpaManagerCdi.getMessageError(e));
        }
        
    }
    
}
