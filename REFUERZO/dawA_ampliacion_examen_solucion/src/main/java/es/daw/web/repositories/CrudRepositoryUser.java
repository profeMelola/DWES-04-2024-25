package es.daw.web.repositories;

import java.util.Optional;
import java.util.Set;

import es.daw.web.entities.User;
import es.daw.web.exceptions.JPAException;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

@RequestScoped
public class CrudRepositoryUser implements CrudRepository<User> {

    @Inject
    EntityManager em;

    @Override
    public Set<User> select() throws JPAException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'select'");
    }

    @Override
    public Optional<User> selectById(int id) throws JPAException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'selectById'");
    }

    @Override
    public Optional<User> selectByPropiedad(User t) throws JPAException {
        // No se puede hacer esto porque solo se puede usar el find con clave primaria
        // return Optional.ofNullable(em.find(User.class,t.getUsername()));

        try {
            if (t.getPassword() == null) {
                return Optional
                        .ofNullable(em.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
                                .setParameter("username", t.getUsername())
                                .getSingleResult());
            } else {
                return Optional.ofNullable(em
                        .createQuery("SELECT u FROM User u WHERE u.username = :username and u.password = :password",
                                User.class)
                        .setParameter("username", t.getUsername())
                        .setParameter("password", t.getPassword())
                        .getSingleResult());
            }
        } catch (NoResultException e) {
            return Optional.empty();
        }
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

    /**
     * 
     * @param username
     * @return
     */
    public boolean isAdmin(String username) {
        // JPQL para verificar si el usuario tiene el rol ADMIN
        String jpql = "SELECT COUNT(u) FROM User u " +
                      "JOIN u.roles r " +
                      "WHERE u.username = :username " +
                      "AND r.roleName = 'ADMIN'";

        Long count = em.createQuery(jpql, Long.class)
                        .setParameter("username", username)
                        .getSingleResult();

        return count > 0; // Si el contador es mayor que 0, el usuario tiene el rol ADMIN        
    }

}
