package es.daw.web.repositories;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import es.daw.web.entities.Fabricante;
import es.daw.web.entities.Producto;
import es.daw.web.exceptions.JPAException;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.FlushModeType;
import jakarta.transaction.Transactional;

/*
 * Se crea una instancia de esta clase por cada solicitud http y se destruye al finalizar la petición
 */
@RequestScoped
public class CrudRepositoryProducto implements CrudRepository<Producto>,Serializable{

    @Inject
    private EntityManager em;

    @Override
    @Transactional
    //@Transactional(readOnly=true) // Solo Spring
    public List<Producto> select() {
        //em.setFlushMode(FlushModeType.COMMIT); // Modo para solo lectura

        // Otra forma con setHint... específico de hibernate
        return em.createQuery("SELECT p FROM Producto p order by p.codigo desc",Producto.class)
                .setHint("org.hibernate.readOnly", true) 
                .getResultList();
    }

    @Override
    @Transactional
    public Optional<Producto> selectById(int id) {
        em.setFlushMode(FlushModeType.COMMIT); // Modo para solo lectura
        return Optional.ofNullable(em.find(Producto.class,id));
    }

    @Override
    @Transactional
    public void deleteById(int id) throws JPAException {
        // Comprobar si exite el fabricante con ese id
        Optional<Producto> p = selectById(id);

        try{
            if (p.isPresent()){
                //em.getTransaction().begin();
                em.remove(p.get());
                em.flush(); // Forzar sincronización con BD
                //em.getTransaction().commit();
            }else{
                throw new JPAException("No existe el producto con codigo "+id+" en la base de datos");
            }
        }catch(Exception e){
            //em.getTransaction().rollback();
            e.printStackTrace();
            throw new JPAException(e.getMessage());
        }
    }

    @Override
    @Transactional
    public void save(Producto t) {
        // Insertar y actualizar
        Optional<Producto> p = selectById(t.getCodigo());

        System.out.println("\n***** producto:"+p.get());

        try{
            //em.getTransaction().begin();
            if (p.isPresent()){
                /*
                 * could not execute statement [La columna "CODIGO_FABRICANTE" no permite valores nulos (NULL)
                    NULL not allowed for column "CODIGO_FABRICANTE"; SQL statement:
                    update producto set codigo_fabricante=?,nombre=?,precio=? where codigo=? [23502-224]] 
                    [update producto set codigo_fabricante=?,nombre=?,precio=? where codigo=?]
                 */
                //em.merge(t); // Da error si el bean tiene campos vacíos, y esos campos son obligatorios...

                System.out.println("\n*********** CRUD_REPOSITORY_PRODUCTO **********");
                System.out.println("* producto a actualizar (parámetro): "+t);
                System.out.println("* producto a actualizar (optional): "+p.get());

                // FORMA 1
                // Pendiente sacar del optional los parámetros y sustituir solo el nombre!!!
                Producto nuevoProducto = p.get();
                nuevoProducto.setNombre(t.getNombre());
                em.merge(nuevoProducto);

                // --------------------------------
                // FORMA 2
                // JPQL (no es HQL... muy similar. En Hibernate se utiliza session.createQuery...)
                // em.createQuery("update Producto p set p.nombre = :nuevo_nombre where p.codigo = :codigo")
                // .setParameter("nuevo_nombre",t.getNombre())
                // .setParameter("codigo", t.getCodigo())
                // .executeUpdate(); //no afecta al contexto de persistencia

                // em.flush(); // Sincronizar todos los cambios pendientes del contexto de persistencia con la base de datos.
                // em.clear(); // Vaciar el contexto de persistencia para que las entidades se recarguen desde la base de datos en la siguiente consulta, reflejando así los cambios recientes.

                // System.out.println("* Hecho flush y clear... ");
                // ----------------------------

            }else{
                em.persist(t);
            }
            //em.getTransaction().commit();
        }catch(Exception e){
            //em.getTransaction().rollback();
            e.printStackTrace();
        }
    }
    
}
