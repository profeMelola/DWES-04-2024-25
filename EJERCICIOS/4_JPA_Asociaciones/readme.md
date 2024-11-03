# Asociaciones @OneToMany y @ManyToOne

El objetivo es probar a persistir un autor y que se guarden automáticamente en la base de datos los libros de dicho autor, debido a la cascada configurada.


## Script de base de datos

Para crear las tablas con datos para las pruebas

```
-- Crear la tabla de autores
CREATE TABLE Author (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

-- Crear la tabla de libros
CREATE TABLE Book (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author_id BIGINT,
    FOREIGN KEY (author_id) REFERENCES Author(id) ON DELETE CASCADE
);

-- Insertar algunos datos de ejemplo en la tabla de autores
INSERT INTO Author (name) VALUES ('Gabriel García Márquez');
INSERT INTO Author (name) VALUES ('Julio Cortázar');

-- Insertar algunos libros asociados a los autores
INSERT INTO Book (title, author_id) VALUES ('Cien años de soledad', 1);
INSERT INTO Book (title, author_id) VALUES ('El amor en los tiempos del cólera', 1);
INSERT INTO Book (title, author_id) VALUES ('Rayuela', 2);
INSERT INTO Book (title, author_id) VALUES ('La vuelta al día en ochenta mundos', 2);

```

## Entidades

Crea una aplicación web que permita trabajar con estas dos entidades:

**Author:**

```
import jakarta.persistence.*;
import java.util.List;

@Entity
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Book> books;

    // Constructores
    public Author() {}
    
    public Author(String name) {
        this.name = name;
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

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
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
}

```

- **@OneToMany(mappedBy = "author"):** significa que un autor tiene una lista de libros. La relación es bidireccional y mappedBy especifica que la entidad Book tiene la clave foránea author en su clase.
- **cascade = CascadeType.ALL:** permite que cualquier operación (persistir, eliminar, actualizar) realizada en un autor se aplique también en sus libros.
- **orphanRemoval = true:** elimina de la base de datos cualquier libro que se elimine de la lista books del autor.
  
____

**Book:**

```
import jakarta.persistence.*;

@Entity
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;

    // Constructores
    public Book() {}

    public Book(String title) {
        this.title = title;
    }

    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }
}

```

- **@ManyToOne:** indica que muchos libros pueden estar asociados a un solo autor.
- **@JoinColumn(name = "author_id"):** define el nombre de la columna en la base de datos que hará la referencia al id del autor.

## Uso de las entidades

Simplemente crea un controlador web que reciba una petición y ejecute el siguiente código:

```
Author author = new Author("Gabriel García Márquez");

Book book1 = new Book("Cien años de soledad");
Book book2 = new Book("El amor en los tiempos del cólera");

// Relacionamos los libros con el autor
author.addBook(book1);
author.addBook(book2);

// Guardamos el autor, automáticamente también guarda los libros asociados
entityManager.persist(author);

```

Comprueba en la base de datos que se han dado de alta los registros correspondientes.
