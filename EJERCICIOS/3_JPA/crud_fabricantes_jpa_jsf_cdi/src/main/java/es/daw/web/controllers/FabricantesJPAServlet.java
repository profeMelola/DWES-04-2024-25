package es.daw.web.controllers;

import java.io.IOException;

import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;
import java.util.ArrayList;

import es.daw.web.entities.Fabricante;
import es.daw.web.exceptions.JPAException;
import es.daw.web.repositories.CrudRepository;
import es.daw.web.repositories.CrudRepositoryFabricante;

@WebServlet("/fabricantes/jpa")
public class FabricantesJPAServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Inject
    CrudRepository<Fabricante> daoF;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // LECTURA DE PARÁMTROS
        String operacion = request.getParameter("operacion");
        String nombre = request.getParameter("nombre");
        String codigo = request.getParameter("codigo");

        // VARIABLES...
        List<Fabricante> fabricantes = new ArrayList<>();

        // LÓGICA ....
        try{
            switch (operacion) {
                //case "select" -> fabricantes = daoF.select();
                case "update" -> {
                    Fabricante fabricante = new Fabricante();
                    fabricante.setNombre(nombre);
                    fabricante.setCodigo(Integer.parseInt(codigo));
                    daoF.save(fabricante);
                }
                case "insert" ->{
                    Fabricante fabricante = new Fabricante();
                    fabricante.setNombre(nombre);
                    daoF.save(fabricante);
                }
                case "delete" -> daoF.deleteById(Integer.parseInt(codigo));
            }
    
        }catch(JPAException e){
            e.printStackTrace();
            request.setAttribute("error", e.getMessage());
            getServletContext().getRequestDispatcher("/error.jsp").forward(request, response);    
        }

        // Siempre se listan los fabricantes
        fabricantes = daoF.select();


        // RESPONSE
        request.setAttribute("operacion", operacion);
        request.setAttribute("fabricantes",fabricantes);

        //getServletContext().getRequestDispatcher("/fabricantes.jsp").forward(request, response);
        getServletContext().getRequestDispatcher("/fabricantes.xhtml").forward(request, response);

    }

}
