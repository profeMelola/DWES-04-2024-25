package es.daw.jakarta.listeners;

import es.daw.jakarta.models.Carro;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.ServletRequestEvent;
import jakarta.servlet.ServletRequestListener;
import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;

@WebListener
public class AppListener implements ServletContextListener,
        ServletRequestListener, HttpSessionListener {

    private ServletContext sc;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        sc = sce.getServletContext();

        sc.log("******** inicializando la aplicacion!");
        sc.setAttribute("mensaje", "algun valor global de la app!");

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        sc.log("******** destruyendo la aplicacion!");
    }

    @Override
    public void requestInitialized(ServletRequestEvent sre) {
        sc.log("******** inicializando el request!");
        sre.getServletRequest().setAttribute("mensaje", "guardando algun valor para el request");
    }

    @Override
    public void requestDestroyed(ServletRequestEvent sre) {
        sc.log("******** destruyendo el request!");
    }

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        sc.log("******** inicializando la sesion http");

        // -------------- CDI ----------
        // YA NO NECESITAMOS CREAR EL OBJETO CARRO NI USAR SETATTRIBUTE PARA METERLO EN LA SESSIÓN
        // LO HACE CDI AUTOMÁTICAMENTE POR MÍ (GRACIAS A LA ANOTACIÓN...)
        // // En vez de crear el carro en el servlet AgregarCarro
        // Carro carro = new Carro();
        // se.getSession().setAttribute("carro", carro);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        sc.log("******** destruyendo la sesion http");
    }
}
