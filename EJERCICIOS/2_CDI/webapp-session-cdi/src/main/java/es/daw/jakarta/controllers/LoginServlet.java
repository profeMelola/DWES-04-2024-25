package es.daw.jakarta.controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

import es.daw.jakarta.services.LoginService;
import es.daw.jakarta.services.LoginServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    final static String USERNAME = "admin";
    final static String PASSWORD = "12345";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        LoginService auth = new LoginServiceImpl();
        Optional<String> usernameOpt = auth.getUserName(request);

        if (usernameOpt.isPresent()){
            response.setContentType("text/html;charset=UTF-8");
            try(PrintWriter out = response.getWriter()){
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");
                out.println("<title>Login</title>");
                out.println("</title>");
                out.println("   <body>");
                out.println("       <h1>Hola "+usernameOpt.get()+" has iniciado sesión con éxito!</h1>");
                out.println("       <p><a href='index.html'>Volver</a></p>");
                out.println("       <p><a href='logout'>Logout</a></p>");
                out.println("   </body>");
                out.println("</html>");
            }
     
        }
        else
            response.sendRedirect("login.html");

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1.leer datos del request
        String login = request.getParameter("login");
        String pwd = request.getParameter("pwd");

        response.setContentType("text/html;charset=UTF-8");

        // 2. Procesarlos

        if (USERNAME.equals(login) && PASSWORD.equals(pwd)){ // hemos ido a BD y comprobado que existe el usuario

            HttpSession session = request.getSession(); 
            session.setAttribute("username", login);           

            doGet(request,response);

        }
        else{
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Lo sentimos no esta autorizado para ingresar a esta página!");
            //response.sendError(HttpServletResponse.SC_UNAUTHORIZED);

        }
 
    }
}