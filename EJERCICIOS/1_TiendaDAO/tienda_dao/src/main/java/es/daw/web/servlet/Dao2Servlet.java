package es.daw.web.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;

import es.daw.web.bd.DBConnection;
import es.daw.web.bd.DaoFabricante;
import es.daw.web.model.Fabricante;
import es.daw.web.model.Producto;
import es.daw.web.util.Utils;

@WebServlet(name = "Dao2Servlet", value = "/Dao2Servlet")
public class Dao2Servlet extends HttpServlet {

    private String pathProperties = "";

    @Override
    public void init() {
        pathProperties = getServletContext().getRealPath("/JDBC.properties");
        System.out.println("pathProperties:" + pathProperties);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        // Recoger datos enviados desde el formulario
        String nombre = request.getParameter("nombre");
        String precio = request.getParameter("precio");
        String codigo_fabricante = request.getParameter("codigo_fabricante");
        String operacion = request.getParameter("operacion");
        String codigo = request.getParameter("codigo");


        // Pendiente...


        // ------------ SALIDA ------------
        response.setContentType("text/html");

        StringBuffer sb = new StringBuffer();
        
        // Pendiente...

        request.setAttribute("filas", sb.toString());
        getServletContext().getRequestDispatcher("/informe.jsp").forward(request, response);

    }

    @Override
    public void destroy() {
        super.destroy();

        try {
            DBConnection.closeConnection();
        } catch (SQLException ex) {
            System.err.println("[processRequest][ERROR AL CERRA LA CONEXIÓN]" + ex.getMessage());
        }

    }
}