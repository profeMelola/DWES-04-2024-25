package es.daw.web.bd;

import java.util.List;
import java.util.Optional;

import es.daw.web.exceptions.JPAException;
import es.daw.web.models.Fabricante;
import jakarta.persistence.EntityManager;

public class DaoJPAFabricante implements DaoJPA<Fabricante>{

    private EntityManager em;

    public DaoJPAFabricante(String entityManagerName){
        em = ManagerJPA.getEntityManager(entityManagerName);
    }
    
    @Override
    public List<Fabricante> select() {
        return em.createQuery("SELECT f FROM Fabricante f",Fabricante.class).getResultList();
    }

    @Override
    public Optional<Fabricante> selectById(int id) {
        return Optional.ofNullable(em.find(Fabricante.class,id));
    }

    @Override
    public void deleteById(int id) throws JPAException {

        // Comprobar si existe el id en la tabla fabricante
        Optional<Fabricante> f = selectById(id);

        try{
            if (f.isPresent()){
                em.getTransaction().begin();
                em.remove(f.get());
                em.getTransaction().commit();
            }else{
                throw new JPAException("No existe el fabricante con código "+id+" en la base de datos");
            }
    
        }catch(Exception e){
            em.getTransaction().rollback();
            e.printStackTrace();
            throw new JPAException(e.getMessage());
        }


    }

    @Override
    public void save(Fabricante t) {

        // Insertar y actualizar

        Optional<Fabricante> f = selectById(t.getCodigo());

        try{
            em.getTransaction().begin();

            if (f.isPresent()){
                // update
                em.merge(t);
            }else{
                // insert
                em.persist(t);
            }

            em.getTransaction().commit();

        }catch(Exception e){
            em.getTransaction().rollback();
            e.printStackTrace();
        }

    }
    
}
