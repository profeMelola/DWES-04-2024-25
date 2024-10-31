package es.daw.web.repositories;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import es.daw.web.entities.Fabricante;
import es.daw.web.exceptions.JPAException;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

/*
 * Se crea una instancia de esta clas por cada solicitud http y se destruye al finalizar la petición
 */
@RequestScoped
public class CrudRepositoryFabricante implements CrudRepository<Fabricante>,Serializable{

    @Inject
    //@PersistenceContext(unitName = "H2DS") // Sí en Spring
    private EntityManager em;

    // public CrudRepositoryFabricante(String entityManagerName){
    //     em = JpaManager.getEntityManager(entityManagerName);
    // }

    // CONSTRUCTOR VACÍO...
    
    @Override
    public List<Fabricante> select() {
        return em.createQuery("SELECT f FROM Fabricante f order by f.codigo desc",Fabricante.class).getResultList();
    }

    @Override
    public Optional<Fabricante> selectById(int id) {
        return Optional.ofNullable(em.find(Fabricante.class,id));
    }

    @Override
    @Transactional
    public void deleteById(int id) throws JPAException {
        // Comprobar si exite el fabricante con ese id
        Optional<Fabricante> f = selectById(id);

        try{
            if (f.isPresent()){
                //em.getTransaction().begin();
                em.remove(f.get());
                em.flush(); // Forzar sincronización con BD
                //em.getTransaction().commit();
            }else{
                throw new JPAException("No existe el fabricante con codigo "+id+" en la base de datos");
            }
        }catch(Exception e){
            //em.getTransaction().rollback();
            e.printStackTrace();
            throw new JPAException(e.getMessage());
        }
    }

    @Override
    @Transactional
    public void save(Fabricante t) {
        // Insertar y actualizar
        System.out.println("\n *********** t.getCodigo():"+t.getCodigo());
        Optional<Fabricante> f = selectById(t.getCodigo());

        try{
            //em.getTransaction().begin();
            if (f.isPresent()){
                System.out.println("\n********* UPDATE *********");
                // update porque el id del fabricante existe
                em.merge(t);
            }else{
                // insert
                System.out.println("\n********* INSERTAR *********");
                em.persist(t);
            }
            //em.getTransaction().commit();
            System.out.println(" *** HECHO EL COMMIT!!!!");
        }catch(Exception e){
            System.out.println(" *** EXCEPTION EN SAVE!!!!");
            //em.getTransaction().rollback();
            e.printStackTrace();
        }
    }
    
}
