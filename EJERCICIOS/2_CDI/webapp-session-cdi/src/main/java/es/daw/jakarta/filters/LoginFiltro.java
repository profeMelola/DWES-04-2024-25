package es.daw.jakarta.filters;

import java.io.IOException;
import java.util.Optional;

import es.daw.jakarta.services.LoginService;
import es.daw.jakarta.services.LoginServiceImpl;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebFilter({"/carro/*", "/productos/*"})
public class LoginFiltro implements Filter{

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
        throws IOException, ServletException {

        LoginService service = new LoginServiceImpl();
        Optional<String> username = service.getUserName((HttpServletRequest) request);
        
        if (username.isPresent()) {
            chain.doFilter(request, response);
        } else {
            ((HttpServletResponse)response).sendError(HttpServletResponse.SC_UNAUTHORIZED,
                    "Lo sentimos no estas autorizado para ingresar a esta pagina!");
        }
    }
    
}
