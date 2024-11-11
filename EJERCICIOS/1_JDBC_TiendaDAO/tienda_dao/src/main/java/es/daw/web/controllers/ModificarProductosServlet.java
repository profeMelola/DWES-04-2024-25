package es.daw.web.controllers;

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
import es.daw.web.bd.Dao;
import es.daw.web.bd.DaoFabricante;
import es.daw.web.bd.DaoProducto;
import es.daw.web.models.Fabricante;
import es.daw.web.models.Producto;
import es.daw.web.util.Utils;

@WebServlet("/productos/modificar")
public class ModificarProductosServlet extends HttpServlet {

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

        Dao<Producto> daoP;

        try {
            daoP = new DaoProducto(pathProperties);
            Producto p = new Producto();
            switch (operacion) {
                case "insert":
                    p.setNombre(nombre);
                    p.setPrecio(Float.parseFloat(precio));
                    p.setCodigo_fabricante(Integer.parseInt(codigo_fabricante));

                    daoP.insert(p);
                    break;
                case "update":
                    p.setNombre(nombre);
                    p.setCodigo(Integer.parseInt(codigo));
            
                    daoP.update(p);
                    break;
                case "delete":
                    daoP.delete(Integer.parseInt(codigo));
                    break;

            
                default:
                    break;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
            System.err.println(e.getErrorCode());

            response.sendError(response.SC_INTERNAL_SERVER_ERROR, e.getMessage());

            request.setAttribute("error", e.getMessage());
            getServletContext().getRequestDispatcher("/error.jsp").forward(request, response);

        }
        


        // ------------ SALIDA ------------
        getServletContext().getRequestDispatcher("/productos/ver").forward(request, response);

    }

    @Override
    public void destroy() {
        super.destroy();

        // try {
        //     DBConnection.closeConnection();
        // } catch (SQLException ex) {
        //     System.err.println("[processRequest][ERROR AL CERRA LA CONEXIÓN]" + ex.getMessage());
        // }

    }
}