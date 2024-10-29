package es.daw.web.controllers;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;
import java.util.ArrayList;

import es.daw.web.bd.DaoJPA;
import es.daw.web.bd.DaoJPAFabricante;
import es.daw.web.exceptions.JPAException;
import es.daw.web.models.Fabricante;

@WebServlet("/fabricantes/jpa")
public class FabricantesJPAServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // LECTURA DE PARÁMTROS
        String operacion = request.getParameter("operacion");
        String nombre = request.getParameter("nombre");
        String codigo = request.getParameter("codigo");

        // VARIABLES...
        List<Fabricante> fabricantes = new ArrayList<>();
        DaoJPA<Fabricante> daoF = new DaoJPAFabricante("H2DS");

        Fabricante fabricante = new Fabricante();
        fabricante.setNombre(nombre);

        // LÓGICA ....
        try{
            switch (operacion) {
                case "select" -> fabricantes = daoF.select();
                case "insert","update" -> daoF.save(fabricante);
                case "delete" -> daoF.deleteById(Integer.parseInt(codigo));
            }
    
        }catch(JPAException e){
            e.printStackTrace();
            request.setAttribute("error", e.getMessage());
            getServletContext().getRequestDispatcher("/error.jsp").forward(request, response);    
        }

        // RESPONSE
        request.setAttribute("fabricantes",fabricantes);
        getServletContext().getRequestDispatcher("/fabricantes.jsp").forward(request, response);

    }

}
