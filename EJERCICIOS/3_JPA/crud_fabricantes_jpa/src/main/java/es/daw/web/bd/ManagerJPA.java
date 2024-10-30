package es.daw.web.bd;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * Clase Singleton.
 * Solo habrá un EntityManagerFactory instanciado en runtime
 */
public class ManagerJPA {
    
    private static EntityManagerFactory emf;

    // De esta forma no se pueden crear objetos ManagerJPA
    private ManagerJPA(){

    }

    /**
     * Método que sirve...
     * @param persistenceUnit Nombre de la unidad de persistencia de persistence.xml
     * @return objeto EntityManager
     */
    public static EntityManager getEntityManager(String persistenceUnit){
        if (emf == null)
           emf = Persistence.createEntityManagerFactory(persistenceUnit);

        return emf.createEntityManager();
    }


    /**
     * 
     */
    public static void destroyEntityManagerFactory(){
        if (emf != null)
            emf.close();   
    }
}
