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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'selectById'");
    }

    @Override
    public void deleteById(int id) throws JPAException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteById'");
    }

    @Override
    public void save(Fabricante t) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'save'");
    }
    
}
