package es.daw.web.filter;

import java.io.IOException;

import es.daw.web.cdi.LoginBean;
import es.daw.web.cdi.ProductoBean;
import jakarta.inject.Inject;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebFilter("/productos.xhtml")
public class PermisosProductosFiltro implements Filter{

    @Inject
    LoginBean login;

    @Inject
    ProductoBean productoBean;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        // SOLO EL ADMINISTRADOR PUEDE LISTAR LOS PRODUCTOS FILTRADOS (produtos.xthml)

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        if (login.canUpdate()){
            chain.doFilter(request, response);
        }else{
            System.out.println("*********** NO AUTORIZADO PARA LISTAR LIBROS FILTRADOS *****************");

            String mensaje="Lo sentimos no estás autorizado para dar listar libros filtrados";
            productoBean.setDescripcionOperacion("Listar todos los productos");
            productoBean.setMensajeError(mensaje);

            req.getRequestDispatcher("/error.xhtml").forward(req, res);   

        }
            
    }


}
