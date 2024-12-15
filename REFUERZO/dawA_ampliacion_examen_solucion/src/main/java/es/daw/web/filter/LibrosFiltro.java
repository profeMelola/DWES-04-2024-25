package es.daw.web.filter;

import jakarta.inject.Inject;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import es.daw.web.cdi.LoginBean;

@WebFilter("/libros/save")
public class LibrosFiltro implements Filter {

    @Inject
    LoginBean login;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        System.out.println("********************** FILTRO PERMISOS *************************");
        
        if (login.canUpdate()){
            chain.doFilter(request, response);
        }else{
            System.out.println("********************** NO AUTORIZADO *************************");
            httpRequest.setAttribute("error", "Lo sentimos no estas autorizado para dar de alta libros!");
            httpRequest.getRequestDispatcher("/main.xhtml").forward(httpRequest, httpResponse);

        }


        
        
    }


}

