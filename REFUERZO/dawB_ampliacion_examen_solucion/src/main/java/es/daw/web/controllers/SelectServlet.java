package es.daw.web.controllers;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/libros/select")
public class SelectServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("******** SELECT SERVLET ********");

        request.setAttribute("resultado", "[SIMULACRO] Listado de libros...");
        getServletContext().getRequestDispatcher("/resultadoEj1.jsp").forward(request, response);
            
    }


}
