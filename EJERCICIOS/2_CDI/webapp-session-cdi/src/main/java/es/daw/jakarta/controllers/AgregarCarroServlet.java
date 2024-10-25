package es.daw.jakarta.controllers;

import java.io.IOException;
import java.util.Optional;

import es.daw.jakarta.models.Carro;
import es.daw.jakarta.models.ItemCarro;
import es.daw.jakarta.models.Producto;
import es.daw.jakarta.services.ProductService;
import es.daw.jakarta.services.ProductServiceImpl;
import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/carro/agregar")
public class AgregarCarroServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // CDI
    // El api CDI se encarga de crear un objeto carro de cada usuario y será único
    @Inject
    private Carro carro;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // RECOGER LOS PARÁMETROS DE LA PETICIÓN URL...
        Long id = Long.parseLong(request.getParameter("id"));


        // LÓGICA DE NEGOCIO....
        // Comprobar si existe el producto. Lo busco por id
        ProductService service = new ProductServiceImpl();

        Optional<Producto> producto = service.buscarPorId(id);

        if (producto.isPresent()){
            // Crear itemCarro. Añadir dicho item al carro
            ItemCarro item = new ItemCarro(1, producto.get());

            // HttpSession session = request.getSession();

            // Carro carro;

            // ----------------------------------
            // FASE I: sin Listener
            // Controlar si ya existe el objeto carro en la sesión
            // if (session.getAttribute("carro") == null){
            //     carro = new Carro();
            //     session.setAttribute("carro", carro);
            // }
            // else{
            //     carro = (Carro) session.getAttribute("carro");
            // }
            // ----------------------------------

            // ----------------------------------
            // FASE II: con Listener
            // carro = (Carro) session.getAttribute("carro");
            // ----------------------------------
            
            carro.addItemCarro(item);
        }

        
        // GENERAR UNA SALIDA DE RESPUESTA
        // PENDIENTE REDIRIGIR AL SERVLET VER CARRO!!!!!!!!!
        //response.sendRedirect("/carro/ver"); // error!!! no encuentra el servlet porque le falta el context path
        //response.sendRedirect(getServletContext().getContextPath()+"/carro/ver");
        //getServletContext().getRequestDispatcher("/carro/ver").forward(request, response);
        //response.sendRedirect(request.getContextPath()+"/carro/ver");

        getServletContext().getRequestDispatcher("/carroJSF.xhtml").forward(request, response);


    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
