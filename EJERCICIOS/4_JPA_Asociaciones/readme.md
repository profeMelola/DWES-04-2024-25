# Asociaciones @OneToMany y @ManyToOne

El objetivo es probar a persistir un autor y que se guarden automáticamente en la base de datos los libros de dicho autor.

La clave foránea (FOREIGN KEY) en author_id conecta cada libro a un autor específico de la tabla Author. 

La restricción ON DELETE CASCADE implica que si se elimina un autor de la tabla Author, todos los libros asociados con ese autor en la tabla Book también serán eliminados automáticamente.


## Base de datos

### Forma 1 (la que vamos a hacer en clase)

Se proporciona la base de datos. Descárgala de este repositorio **libros.mv.db**

La url de conexión será: **jdbc:h2:~/libros;AUTO_SERVER=TRUE**

Por tanto copia la base de datos en tu directorio home.

Esta base de datos tiene las tablas, datos y cambiada la contraseña del usuario sa.

### Forma 2 (NO hacer en clase!!!!)

Para crear las tablas con datos para las pruebas debes crear un nuevo schema de base de datos en H2.

Cambia la constraseña del usuario sa para poder dar de alta el Datasource en Wildfly (standalone.xml):


```
ALTER USER sa SET PASSWORD 'sa';
```

Este es el script de la base de datos para crear las tablas y datos:


```
-- Crear la tabla de autores
CREATE TABLE Author (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

-- Crear la tabla de libros con un campo de fecha de publicación
CREATE TABLE Book (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author_id BIGINT,
    publication_date DATE,  -- Nuevo campo para la fecha de publicación
    FOREIGN KEY (author_id) REFERENCES Author(id) ON DELETE CASCADE
);

-- Insertar algunos datos de ejemplo en la tabla de autores
INSERT INTO Author (name) VALUES ('Gabriel García Márquez');
INSERT INTO Author (name) VALUES ('Julio Cortázar');

-- Insertar algunos libros asociados a los autores con fechas de publicación
INSERT INTO Book (title, author_id, publication_date) VALUES ('Cien años de soledad', 1, DATE '1967-06-05');
INSERT INTO Book (title, author_id, publication_date) VALUES ('El amor en los tiempos del cólera', 1, DATE '1985-04-05');
INSERT INTO Book (title, author_id, publication_date) VALUES ('Rayuela', 2, DATE '1963-06-28');
INSERT INTO Book (title, author_id, publication_date) VALUES ('La vuelta al día en ochenta mundos', 2, DATE '1970-10-01');

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

    @Temporal(TemporalType.DATE)
    private Date publicationDate;  // Nuevo campo para la fecha de publicación

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

    public Date getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(Date publicationDate) {
        this.publicationDate = publicationDate;
    }
}

```

- **@ManyToOne:** indica que muchos libros pueden estar asociados a un solo autor.
- **@JoinColumn(name = "author_id"):** define el nombre de la columna en la base de datos que hará la referencia al id del autor.

## Uso de las entidades

Simplemente crea un controlador web que reciba una petición y basándote en el siguiente código realice la persistencia del autor y sus libros:

```
// Suponiendo que ya tienes una instancia de EntityManager
EntityManager entityManager = ...;

// Crear un nuevo autor usando el constructor vacío
Author newAuthor = new Author();
newAuthor.setName("Jorge Luis Borges");

// Crear nuevos libros usando el constructor vacío
Book book1 = new Book();
book1.setTitle("Ficciones");
book1.setPublicationDate(Date.valueOf("1944-10-01")); // Fecha de publicación (opcional)

Book book2 = new Book();
book2.setTitle("El Aleph");
book2.setPublicationDate(Date.valueOf("1949-06-01")); // Fecha de publicación (opcional)

// Establecemos la relación entre el autor y los libros
newAuthor.addBook(book1);
newAuthor.addBook(book2);

// Persistimos el nuevo autor (y sus libros asociados)
entityManager.persist(newAuthor);

```

Comprueba en la base de datos que se han dado de alta los registros correspondientes.
