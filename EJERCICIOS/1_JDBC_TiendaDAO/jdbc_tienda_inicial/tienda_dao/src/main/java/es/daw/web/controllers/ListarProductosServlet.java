package es.daw.web.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import es.daw.web.bd.DaoFabricante;
import es.daw.web.models.Fabricante;
import es.daw.web.models.Producto;
import es.daw.web.util.Utils;


@WebServlet("/productos/ver")
public class ListarProductosServlet extends HttpServlet {

    private String pathProperties = "";

    @Override
    public void init() {
        pathProperties = getServletContext().getRealPath("/JDBC.properties");
        System.out.println("pathProperties:"+pathProperties);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {



        // ------------ SALIDA ------------
        response.setContentType("text/html");

        StringBuffer sb = new StringBuffer();


        // Pendiente...

        request.setAttribute("filas", sb.toString());
        getServletContext().getRequestDispatcher("/informe.jsp").forward(request, response);

    }

    @Override
    public void destroy() {
    }
}