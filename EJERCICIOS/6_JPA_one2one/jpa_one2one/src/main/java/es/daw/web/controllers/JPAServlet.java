package es.daw.web.controllers;

import java.io.IOException;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@WebServlet("/initDatabase")
public class JPAServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Inject
    EntityManager em;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

		// pendiente completar!!!!
		em.getTransaction().begin();
		em.getTransaction().commit();

    }

}
