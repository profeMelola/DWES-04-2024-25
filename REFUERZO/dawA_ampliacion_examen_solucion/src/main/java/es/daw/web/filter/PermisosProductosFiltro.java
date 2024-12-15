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

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        if (login.isAdmin()){
            // Si eres administrador
            chain.doFilter(req,res);
        }else{
        // Si no eres administrador que salga mensaje en error.xhtml
            String mensaje = "Lo sentios, no estás autorizado para listar libros filtrados";
            productoBean.setMensajeError(mensaje);
            productoBean.setDescripcionOperacion("No autorizado al listar....");
            req.getRequestDispatcher("/error.xhtml").forward(req, res);

        }


    }
    
}
