package es.daw.web.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@WebFilter("/*")
public class ContadorVisitasFilter implements Filter {

    int visitas = 0;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Inicialización si es necesaria
        visitas = 1;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        //int visitas = 1;

        // Cargar array de cookies
        Cookie[] cookies = httpRequest.getCookies() != null ? httpRequest.getCookies() : new Cookie[0];

        //Optional<Cookie> miCookie = Arrays.stream(cookies)

        // Optional<String> miCookie = Arrays.stream(cookies)
        //              .filter(c -> "num_visitas".equals(c.getName()))
        //              .map(Cookie::getValue)
        //              .findAny();        

        Optional<Cookie> miCookie = Arrays.stream(cookies)
                     .filter(c -> "num_visitas".equals(c.getName()))
                     .findAny(); 
                     
        if (miCookie.isEmpty()){
            // la primera vez es 1 el número de visitas
            //System.out.println("******** COOKIE, PRIMERA VEZ!!!!");
            Cookie newCookie = new Cookie("num_visitas",String.valueOf(visitas));
            newCookie.setMaxAge(60 * 60 * 1); // tiempo de vida 1 hora
            newCookie.setPath("/"); // hacer accesible la cookie a toda la aplicación
            httpResponse.addCookie(newCookie);
        }else{
            // ya existe la cookie
            // obtengo el valor de la cookie
            visitas = Integer.parseInt(miCookie.get().getValue());

            //System.out.println(" ******** COOKIE, visitas: "+visitas);
            visitas++;

            //machacar
            //miCookie.get().setValue(""+visitas);
            miCookie.get().setValue(String.valueOf(visitas));

            httpResponse.addCookie(miCookie.get());

        }


        // System.out.println("*******************************************************************");
        // System.out.println("********************** (FILTRO + COOKIE ) *************************");
        // System.out.println("**********     NÚMERO DE VISITAS: "+visitas+"    ***************************");
        // System.out.println("*******************************************************************");

        // siempre pasa el request!!!
        chain.doFilter(request, response);
        
    }

    @Override
    public void destroy() {
        // Limpieza si es necesaria
    }
}

