package es.daw.web.repositories;

import java.io.Serializable;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.Disposes;
import jakarta.enterprise.inject.Produces;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/*
 * Estará disponible a lo largo de toda la vida de la aplicación
 * EntityManagerFactory es thread-safe. 
 * Diseñado para ser compartido (programación concurrente)
 * CDI crea un JpaManagerCdi cuando se inicia la app web
 */
 @ApplicationScoped
public class JpaManagerCdi implements Serializable{
    
    private EntityManagerFactory emf;

    // Necesita CDI un constructor vacío

    /**
     * Inicializa el EntityManagerFactory al crear la instancia
     * Solo al inicio de la aplicación. Se ejecuta una vez
     */
    @PostConstruct
    public void init(){
        if (emf == null)
           emf = Persistence.createEntityManagerFactory("ClienteDS");

    }


    /**
     * Produce un EntityManager por cada request (petición)
     * Produces se usar para producir servicios
     * Garantizamos que cada instancia esté aislada y sea utilizada en un único hilo (petición)
     * CDI administra su ciclo de vida y espera a un método con @Dispose para saber cómo liberar
     * @param persistenceUnit
     * @return
     */
    @Produces
    @RequestScoped
    public EntityManager getEntityManager(){
        return emf.createEntityManager();
    }

    /**
     * Cierra el EntityManager al finalizar el request
     * Asociado al ciclo de vida de la instancia inyectada
     * @param em 
     */
    public void closeEntityManager(@Disposes EntityManager em){
        if (em.isOpen())
            em.close();
    }

    /**
     * Este método se ejecuta justo antes de que la app se cierre
     */
    @PreDestroy
    public void destroyEntityManagerFactory(){
        if (emf != null && emf.isOpen())
            emf.close();   
    }



}
