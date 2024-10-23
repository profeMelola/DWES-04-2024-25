package es.daw.web.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import es.daw.web.bd.Dao;
import es.daw.web.bd.DaoFabricante;
import es.daw.web.bd.DaoProducto;
import es.daw.web.models.ComparatorProdByName;
import es.daw.web.models.Fabricante;
import es.daw.web.models.Producto;
import es.daw.web.util.Utils;


@WebServlet("/productos/ver")
public class ListarProductosServlet extends HttpServlet {

    private String pathProperties = "";

    @Override
    public void init() {
        pathProperties = getServletContext().getRealPath("/JDBC.properties");
        System.out.println("pathProperties:"+pathProperties); // Ruta absoluta del webapp
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        Dao daoP, daoF;

        List<Producto> productos = null;
        List<Fabricante> fabricantes = null;

        try {
            daoP = new DaoProducto(pathProperties);
            daoF = new DaoFabricante(pathProperties);

            productos = daoP.selectAll();
            productos.forEach(System.out::println);

            fabricantes = daoF.selectAll();



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


        // --------------------------------------------------
        // ---------------- ORDENACIONES --------------------
        //Collections.sort(productos); // ordenación por código ascendente
        //productos.sort(Comparator.naturalOrder()); // por código ascendente

        //Collections.reverse(productos); // por código descendente
        //productos.sort(Comparator.reverseOrder());

        // ORDENAR POR NOMBRE DE PRODUCTO
        //productos.sort(new ComparatorProdByName()); // por nombre asc

        //productos.sort(new ComparatorProdByName().reversed()); // desc

        //productos.sort((p1,p2) -> p1.getNombre().compareTo(p2.getNombre()));

        // Por precio descendente
        // productos.sort((p1,p2) -> Float.compare(p1.getPrecio(), p2.getPrecio()));
        // Collections.reverse(productos);

        // por nombre y en caso de empate por precio
        productos.sort(new ComparatorProdByName().thenComparing((p1,p2) -> Float.compare(p1.getPrecio(), p2.getPrecio())));
        
        
        // --------------------------------------------------

        for (Producto p : productos) {
            sb.append("<tr><td>").append(p.getCodigo()).append("</td>")
            .append("<td>").append(p.getNombre()).append("</td>")
            .append("<td>").append(p.getPrecio()).append("</td>");


            //.append("<td>").append(p.getCodigo_fabricante()).append("</td></tr>");

            // Obtener el nombre del fabricante
            // Forma 1: vía BD
            // select nombre from fabricante where codigo = ?

            // Forma 2: vía programación
            String nombreFabricante = Utils.obtenerNombreFabricante(fabricantes, p.getCodigo_fabricante());
            sb.append("<td>").append(nombreFabricante).append("</td></tr>");
        }

        request.setAttribute("filas", sb.toString());
        getServletContext().getRequestDispatcher("/informe.jsp").forward(request, response);

    }

    @Override
    public void destroy() {
    }
}