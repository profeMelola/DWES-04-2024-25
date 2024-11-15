package es.daw.web.controllers;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;


@WebServlet("/initDatabase")
//@Transactional
public class JPAServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Inject
    EntityManager em;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

      try{
        //pendiente completar!!!!
        System.out.println("*********** INICIO TRANSACCIÓN ***********");
        em.getTransaction().begin();
        em.getTransaction().commit();
        System.out.println("*********** COMMIT TRANSACCIÓN ***********");

        // System.out.println("************* TRANSACTIONAL **************");
        getServletContext().getRequestDispatcher("/clientes/save").forward(request, response);

      }catch(Exception e){
        e.printStackTrace();
        em.getTransaction().rollback();
        response.sendError(response.SC_INTERNAL_SERVER_ERROR,e.getMessage());
      }

      // No error. Mensajito de texto, nada de html
      response.setContentType("text/plain");
      //response.getWriter().write("La estructura de tablas se han creado correctamente en la base de datos");

      try(PrintWriter out = response.getWriter()){
        out.println("La estructura de tablas se han creado correctamente en la base de datos");
      }
        

    }

}
