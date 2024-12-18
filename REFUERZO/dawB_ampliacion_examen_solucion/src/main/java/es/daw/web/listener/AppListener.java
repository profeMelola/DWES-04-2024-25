package es.daw.web.listener;


import es.daw.web.cdi.LoginBean;
import jakarta.inject.Inject;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.ServletRequestEvent;
import jakarta.servlet.ServletRequestListener;
import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;

@WebListener
public class AppListener implements ServletContextListener,
        ServletRequestListener, HttpSessionListener {

    private ServletContext sc;

    @Inject
    LoginBean login;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        sc = sce.getServletContext();

        sc.log("*******************************************");
        sc.log("******** inicializando la aplicacion!");
        sc.log("*******************************************");
        sc.setAttribute("mensaje_global", "algun valor global de la app!");

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        sc.log("******** destruyendo la aplicacion!");
    }

    @Override
    public void requestInitialized(ServletRequestEvent sre) {
        
        sc.log("*************************************");
        sc.log("******** inicializando el request!");
        sc.log("USUARIO LOGIN: "+login.getUsername());
        sc.log("*************************************");

        sre.getServletRequest().setAttribute("mensaje", "guardando algun valor para el request");
    }

    @Override
    public void requestDestroyed(ServletRequestEvent sre) {
        sc.log("******** destruyendo el request!");
    }

    @Override
    public void sessionCreated(HttpSessionEvent se) {

        sc.log("*************************************");
        sc.log("******** inicializando la sesion http");
        sc.log("USUARIO LOGIN: "+login.getUsername());
        sc.log("*************************************");

        // En vez de crear el carro en el servlet AgregarCarro
        // Carro carro = new Carro();
        // se.getSession().setAttribute("carro", carro);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        sc.log("************************************");
        sc.log("******** destruyendo la sesion http");
        sc.log("USUARIO LOGIN: "+login.getUsername());
        sc.log("************************************");
    }
}
