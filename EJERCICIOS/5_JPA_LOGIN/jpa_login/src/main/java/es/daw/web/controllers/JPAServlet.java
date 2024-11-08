package es.daw.web.controllers;

import java.io.IOException;
import java.time.LocalDate;

import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Set;
import java.util.LinkedHashSet;

import es.daw.web.entities.Author;
import es.daw.web.entities.Book;
import es.daw.web.exceptions.JPAException;
import es.daw.web.repositories.CrudRepository;

@WebServlet("/libros/jpa")
public class JPAServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Inject
    CrudRepository<Book> daoBook;

    @Inject
    CrudRepository<Author> daoAuthor;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Recoger datos enviados desde el formulario
        String operacion = request.getParameter("operacion");

        // VARIABLES...
        Set<Book> libros = new LinkedHashSet<>();

        try {
            switch (operacion) {
                case "select" -> {
                    libros = daoBook.select();
                }
                case "save" -> {
                    // Crear un nuevo autor usando el constructor vacío
                    Author newAuthor = new Author();
                    newAuthor.setName("Jorge Luis Borges");

                    // Crear nuevos libros usando el constructor vacío
                    Book book1 = new Book();
                    book1.setTitle("Ficciones");
                    book1.setPublicationDate(LocalDate.of(1944,10,01)); // Fecha de publicación (opcional)

                    Book book2 = new Book();
                    book2.setTitle("El Aleph");
                    book2.setPublicationDate(LocalDate.parse("1949-06-01")); // Fecha de publicación (opcional)

                    // Establecemos la relación entre el autor y los libros
                    newAuthor.addBook(book1);
                    newAuthor.addBook(book2);

                    daoAuthor.save(newAuthor);
                }
            }

        } catch (JPAException e) {
            e.printStackTrace();
            request.setAttribute("error", e.getMessage());
            getServletContext().getRequestDispatcher("/error.jsp").forward(request, response);
        }

        // RESPONSE
        request.setAttribute("libros", libros);
        // Si es listar, libros tendrá los registros de BD
        // Si es save, libros estará vacío
        getServletContext().getRequestDispatcher("/libros.xhtml").forward(request, response);

    }

}
