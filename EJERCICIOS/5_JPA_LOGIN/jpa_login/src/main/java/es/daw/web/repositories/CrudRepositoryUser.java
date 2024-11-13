package es.daw.web.repositories;

import java.util.Optional;
import java.util.Set;

import es.daw.web.entities.Rol;
import es.daw.web.entities.User;
import es.daw.web.exceptions.JPAException;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

@RequestScoped
public class CrudRepositoryUser implements CrudRepository<User>{

    @Inject
    EntityManager em;

    @Override
    public Set<User> select() throws JPAException {

        // PODRÍA UTILIZAR ESTE MÉTODO Y DEVOLVER SOLO UN OBJETO USER EN EL SET
        throw new UnsupportedOperationException("Unimplemented method 'select'");

    }

    @Override
    public Optional<User> selectById(int id) throws JPAException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'selectById'");
    }

    @Override
    public void deleteById(int id) throws JPAException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteById'");
    }

    @Override
    public void save(User t) throws JPAException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'save'");
    }

    @Override
    public Optional<User> selectByPropiedad(User t) throws JPAException {

        try{

            String jpql = "";
            if (t.getPassword() == null){
                System.out.println("************ USUARIO A REVISAR LOGIN **********");
                System.out.println("*********** usuario: "+t);
                jpql = "SELECT u FROM User u WHERE u.username = :username";
    
                return Optional.ofNullable(em.createQuery(jpql,User.class)
                    .setParameter("username", t.getUsername())
                    .getSingleResult());
            }
            else{
                jpql = "SELECT u FROM User u WHERE u.username = :username and password = :password";
                return Optional.ofNullable(em.createQuery(jpql,User.class)
                .setParameter("username", t.getUsername())
                .setParameter("password", t.getPassword())
                .getSingleResult());
            }


        }catch(NoResultException nre){
            return Optional.empty();
        }
        catch(Exception e){
            e.printStackTrace();
            throw new JPAException(JpaManagerCdi.getMessageError(e));
        }
        
            
    }

    /**
     * 
     * @param t
     * @return
     */
    public boolean isAdmin(User t){
        String jpql = "SELECT COUNT(u) FROM User u "+
        "JOIN u.roles r "+
        "WHERE u.username = :username " +
        "AND r.rolename = 'ADMIN'";


        Long count = em.createQuery(jpql,Long.class)
                    .setParameter("username", t.getUsername())
                    .getSingleResult();

        return count > 0;

    }

    /**
     * 
     * @param t
     * @param r
     * @return
     */
    public boolean hasRole(User t,String rol){
        String jpql = "SELECT COUNT(u) FROM User u "+
        "JOIN u.roles r "+
        "WHERE u.username = :username " +
        "AND r.rolename = '"+rol+"'";


        Long count = em.createQuery(jpql,Long.class)
                    .setParameter("username", t.getUsername())
                    .getSingleResult();

        return count > 0;

    }

    
    
    
}
