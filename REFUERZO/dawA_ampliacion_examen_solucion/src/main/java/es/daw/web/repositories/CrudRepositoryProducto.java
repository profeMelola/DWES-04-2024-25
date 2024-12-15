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
        try{
            Producto p = em.find(Producto.class, id);
            return Optional.ofNullable(p);
        }catch(Exception e){
            throw new JPAException(JpaManagerCdi.getMessageError(e));
        }
    }

    @Override
    @Transactional
    public void deleteById(int id) throws JPAException {
            
        try{
            Optional<Producto> producto = selectById(id);
            // if (producto.isPresent()){
            //     em.remove(id);
            //     em.flush();
            // }
    
            producto.ifPresent(p ->{
                em.remove(p);
                em.flush();
            });
            
        }catch(Exception e){
            throw new JPAException(JpaManagerCdi.getMessageError(e));
        }
        
    }

    @Override
    @Transactional
    public void save(Producto t) throws JPAException {
        try{

            if (t.getId() == null)
                em.persist(t);
            else{
                if (selectById(t.getId().intValue()).isPresent())
                    em.merge(t);
            }
            // // al insertar fallaría porque el producto no tiene id
            // if (selectById(t.getId().intValue()).isPresent())
            //     em.merge(t);
            // else
            //     em.persist(t);

            em.flush();
        }catch(Exception e){
            throw new JPAException(JpaManagerCdi.getMessageError(e));
        }
        
    }

    @Override
    public Set<Producto> selectAll() throws JPAException {
        try{
            String jpql = "SELECT p FROM Producto p";
        
            List<Producto> lista = em.createQuery(jpql,Producto.class)
            .getResultList();
    
            Set<Producto> productos = new LinkedHashSet<>(lista);

            return productos;
    
        }catch(Exception e){
            System.err.println("************** ERROR AL LISTAR TODOS ************");
            //e.printStackTrace();
            throw new JPAException(JpaManagerCdi.getMessageError(e));
        }

    }
    
    
}
