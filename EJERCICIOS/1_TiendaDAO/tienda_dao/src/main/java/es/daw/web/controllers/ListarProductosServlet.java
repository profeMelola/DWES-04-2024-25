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
import es.daw.web.bd.DaoProducto;
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

        DaoProducto daoP;

        List<Producto> productos = null;

        try {
            daoP = new DaoProducto(pathProperties);
            productos = daoP.selectAll();
            productos.forEach(System.out::println);

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
            System.err.println(e.getErrorCode());

            response.sendError(response.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }

        // SI NO HA HABIDO ERROR, PASA POR AQUÍ!!!!

        // ------------ SALIDA ------------
        response.setContentType("text/html;charset=UTF-8");

        StringBuilder sb = new StringBuilder();
        // pendiente... montar el sb con los tr y td

        for (Producto p : productos) {
            sb.append("<tr><td>").append(p.getCodigo()).append("</td>")
            .append("<td>").append(p.getNombre()).append("</td>")
            .append("<td>").append(p.getPrecio()).append("</td>")
            .append("<td>").append(p.getCodigo_fabricante()).append("</td></tr>");
        }

        request.setAttribute("filas", sb.toString());
        getServletContext().getRequestDispatcher("/informe.jsp").forward(request, response);

    }

    @Override
    public void destroy() {
    }
}