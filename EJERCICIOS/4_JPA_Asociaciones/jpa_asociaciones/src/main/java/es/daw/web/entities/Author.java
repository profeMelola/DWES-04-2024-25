package es.daw.web.entities;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    /*
     Una instancia de Author puede estar asociada con múltiples instancias de Book
     Un autor tiene una lista de libros. 

    
     En la clase Book, hay un campo que se llama author, que representa la relación con el autor.
     La relación es bidireccional y mappedBy especifica que la entidad Book tiene la clave foránea author en su clase.
     Al definir mappedBy, le estamos diciendo a JPA que Book es el dueño de la relación 
     es decir, tiene la clave foránea author_id. 
     En el lado de Author, no se necesita tener una columna que almacene esta clave foránea, ya que se gestiona en Book.

     CascadeType.ALL: permite que cualquier operación (persistir, eliminar, actualizar) realizada en un autor se aplique también en sus libros.
     orphanRemoval = true: elimina de la base de datos cualquier libro que se elimine de la lista books del autor.

     */

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Book> books;

    // Constructores
    public Author() {
        books = new HashSet<>();
    }
    
    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Book> getBooks() {
        return books;
    }

    public void setBooks(Set<Book> books) {
        this.books = books;
    }

    public void addBook(Book book) {
        books.add(book);
        book.setAuthor(this);
    }

    public void removeBook(Book book) {
        books.remove(book);
        book.setAuthor(null);
    }

    @Override
    public String toString() {
        return "Author [id=" + id + ", name=" + name + "]";
    }

    
}

