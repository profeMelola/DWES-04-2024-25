package es.daw.jakarta.controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import es.daw.jakarta.services.ProductServiceImpl;
import es.daw.jakarta.models.Producto;
import es.daw.jakarta.services.ProductService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/productos")
public class ProductServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Obtener un objeto del servicio
        ProductService service = new ProductServiceImpl();

        // Obtener la lista de productos
        List<Producto> productos = service.listar();

        // Generar tabla HTML a partir de una JSP

        request.setAttribute("productos", productos);
        request.getRequestDispatcher("/productos.jsp").forward(request, response);
                
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO: implement POST request handling
        //doGet(request, response);
    }
}
