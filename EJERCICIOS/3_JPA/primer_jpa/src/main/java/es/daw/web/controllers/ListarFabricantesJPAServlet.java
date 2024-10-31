package es.daw.web.controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import es.daw.web.models.Fabricante;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/fabricantes/jpa/ver")
public class ListarFabricantesJPAServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private EntityManagerFactory emf;
    
    @Override
    public void init() throws ServletException {
        // inicializamos el emf 
        emf = Persistence.createEntityManagerFactory("H2DS");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Fabricante> fabricantes;

        try(EntityManager em = emf.createEntityManager()){
            //JPQL
            // TypedQuery es una clase de JPA que se utiliza para definir y ejecutar consultas con tipo (tipo Fabricante). 
            // Indica que se debe seleccionar (SELECT) la entidad Fabricante, alias f, de la base de datos
            fabricantes = em.createQuery("SELECT f FROM Fabricante f",Fabricante.class).getResultList();


            System.out.println("*****************");
            fabricantes.forEach(System.out::println);
            System.out.println("*****************");
        }

        request.setAttribute("fabricantes", fabricantes);
        getServletContext().getRequestDispatcher("/fabricantes.jsp").forward(request, response);
    }

    @Override
    public void destroy() {
        // Cierro el emf
        if (emf != null)
            emf.close();
    }
}
