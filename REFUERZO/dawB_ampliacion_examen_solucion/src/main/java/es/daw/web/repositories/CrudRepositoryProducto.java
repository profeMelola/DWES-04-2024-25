package es.daw.web.repositories;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import es.daw.web.entities.Producto;
import es.daw.web.exceptions.JPAException;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.Model;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

//@RequestScoped
//@Named
@Model
public class CrudRepositoryProducto implements CrudRepository<Producto>{

    @Inject
    EntityManager em;

    @Override
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
            throw new JPAException(JpaManagerCdi.getMessageError(e));
        }

    }

    @Override
    public Optional<Producto> selectById(Long id) throws JPAException {
        try{
            Producto p = em.find(Producto.class, id);
            return Optional.ofNullable(p);
        }catch(Exception e){
            throw new JPAException(JpaManagerCdi.getMessageError(e));
        }
    }

    @Override
    @Transactional
    public void deleteById(Long id) throws JPAException {
        Optional<Producto> producto = selectById(id);
        if (producto.isPresent()){
            em.remove(producto.get());
        }
        em.flush();
    }

    @Override
    @Transactional
    public void save(Producto t) throws JPAException {
        try{
            // COntrolar si es persist o merge!!!
            if (t.getId() == null)
                em.persist(t); // inserción
            else{
                if (selectById(t.getId()).isPresent())
                    em.merge(t);
                else{
                    new JPAException("El id del producto es corrupto!!!!! ");
                    //em.persist(t); // no sé de dónde vendría un id que no existe en la tabla, pero decido insertar el objeto...
                }
            }
            // if (selectById(t.getId()).isPresent())
            //     em.merge(t); //actualización
            // else
            //     em.persist(t); // inserción
            
            em.flush(); // fuerza la persistencia. Así aparecen los productos actualizados en la lista...
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

            System.out.println("************ CRUD SELECT ALL **********");
            productos.forEach(System.out::println);
            System.out.println("***********************************");
    
            return productos;
    
        }catch(Exception e){
            throw new JPAException(JpaManagerCdi.getMessageError(e));
        }

    }

    @Override
    public Optional<Producto> selectByPropiedad(Producto t) throws JPAException {
        // TODO Auto-generated method stub
        return CrudRepository.super.selectByPropiedad(t);
    }

    
    
}
