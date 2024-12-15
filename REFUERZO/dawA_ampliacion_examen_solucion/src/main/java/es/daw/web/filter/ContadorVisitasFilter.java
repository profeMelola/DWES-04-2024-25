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
        visitas = 1;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

                //int visitas = 0;
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        
        // Busca la cookie "num_visitas"
        Cookie[] cookies = httpRequest.getCookies() != null ? httpRequest.getCookies() : new Cookie[0];
        
        Optional<Cookie> miCookie = Arrays.stream(cookies)
                            .filter(c -> "num_visitas".equals(c.getName()))
                            .findAny();

        if (miCookie.isEmpty()){
            // Si no existe la cookie, la creamos con un valor incial de 1
            Cookie newCookie = new Cookie("num_visitas",String.valueOf(visitas));
            //Cookie newCookie = new Cookie("num_visitas",""+visitas);
            newCookie.setPath("/");
            newCookie.setMaxAge(60* 60 * 1);// tiempo de vida: 1 hora (en segundos)
            httpResponse.addCookie(newCookie);

        }else{
            // Si ya existe, incrementaos su valor y volveos a addCookie
            visitas = Integer.parseInt(miCookie.get().getValue());
            visitas++;
            miCookie.get().setPath("/");
            miCookie.get().setValue(String.valueOf(visitas));
            httpResponse.addCookie(miCookie.get());
        }



        // System.out.println("*******************************************************************");
        // System.out.println("********************** (FILTRO + COOKIE ) *************************");
        // System.out.println("**********     NÚMERO DE VISITAS: "+visitas+"    ***************************");
        // System.out.println("*******************************************************************");

        chain.doFilter(request, response);
        
    }

    @Override
    public void destroy() {
        // Limpieza si es necesaria
    }
}

