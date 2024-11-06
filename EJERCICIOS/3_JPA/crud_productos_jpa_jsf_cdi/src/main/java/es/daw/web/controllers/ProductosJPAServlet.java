package es.daw.web.controllers;

import java.io.IOException;

import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

import es.daw.web.entities.Fabricante;
import es.daw.web.entities.Producto;
import es.daw.web.exceptions.JPAException;
import es.daw.web.repositories.CrudRepository;

@WebServlet("/productos/jpa")
public class ProductosJPAServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Inject
    CrudRepository<Producto> daoP;

    @Inject
    CrudRepository<Fabricante> daoF;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Recoger datos enviados desde el formulario
        String nombre = request.getParameter("nombre");
        String precio = request.getParameter("precio");
        String codigo_fabricante = request.getParameter("codigo_fabricante");
        String operacion = request.getParameter("operacion");
        String codigo = request.getParameter("codigo");        

        // VARIABLES...
        List<Producto> productos = new ArrayList<>();
        Producto producto = new Producto();
        producto.setNombre(nombre);

        Optional<Fabricante> fabricanteOpt = daoF.selectById(Integer.parseInt(codigo_fabricante));
        // Comprobar que el código del fabricante existe en la base de datos...
        if (fabricanteOpt.isEmpty()){
            // OPCIONES!!!!!
            // Si el fabricante no existe podemos crearlo????? No tenemos el nombre

            // Si no existe indicárselo al usuario....

        }
        // LÓGICA ....
        try{
            switch (operacion) {
                case "update" -> {
                    producto.setCodigo(Integer.parseInt(codigo));
                    // porque el fabricante es obligatorio en la tabla producto...
                    // pero no es la solución, porque solo quiero actualizar por nombre y se me machaca con precio 0
                    // y el primer fabricante
                    //producto.setFabricante(fabricanteOpt.get()); 
                    daoP.save(producto);
                }
                case "insert" ->{
                    producto.setPrecio(Float.parseFloat(precio));
                    producto.setFabricante(fabricanteOpt.get());
                    daoP.save(producto);
                }
                case "delete" -> daoP.deleteById(Integer.parseInt(codigo));
            }
    
        }catch(JPAException e){
            e.printStackTrace();
            request.setAttribute("error", e.getMessage());
            getServletContext().getRequestDispatcher("/error.jsp").forward(request, response);    
        }

        // Siempre se listan los productos
        productos = daoP.select();


        // RESPONSE
        request.setAttribute("operacion", operacion);
        request.setAttribute("productos",productos);

        getServletContext().getRequestDispatcher("/reports/productos.xhtml").forward(request, response);

    }

}
