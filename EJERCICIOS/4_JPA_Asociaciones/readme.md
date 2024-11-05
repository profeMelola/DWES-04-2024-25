# EJERCICIO: Asociaciones @OneToMany y @ManyToOne

El objetivo es probar a persistir un autor y que se guarden automáticamente en la base de datos los libros de dicho autor.

La clave foránea (FOREIGN KEY) en author_id conecta cada libro a un autor específico de la tabla Author. 

La restricción ON DELETE CASCADE implica que si se elimina un autor de la tabla Author, todos los libros asociados con ese autor en la tabla Book también serán eliminados automáticamente.


## 1. Base de datos

### Forma 1: usar un schema ya creado (la que vamos a usar en clase)

Se proporciona la base de datos. Descárgala de este repositorio **libros.mv.db**

La url de conexión será: **jdbc:h2:~/libros;AUTO_SERVER=TRUE**

Por tanto copia la base de datos en tu directorio home.

Esta base de datos tiene las tablas, datos y cambiada la contraseña del usuario sa.

### Forma 2: crear el schema de cero (NO usar en clase!!!!)

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

## 2. Datasource

Configura el nuevo Datasource.

Edita el archivo de configuración **standalone.xml** y añade el siguiente datasource:

```
                <datasource jndi-name="java:/LibrosDS" pool-name="LibrosDS">
                    <connection-url> jdbc:h2:~/libros;AUTO_SERVER=TRUE</connection-url>
                    <driver-class>org.h2.Driver</driver-class>
                    <driver>h2</driver>
                    <security user-name="sa" password="sa"/>
                </datasource>

```

Reinicia Wildfly.

## 3.persistence.xml

Configura adecuadamente este fichero con el nombre del DataSource recién creado y las entidades.

```
<persistence xmlns="https://jakarta.ee/xml/ns/persistence"
             xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xsi:schemaLocation="https://jakarta.ee/xml/ns/persistence https://jakarta.ee/xml/ns/persistence/persistence_3_0.xsd"
             version="3.0">
    <persistence-unit name="LibrosDS" transaction-type="JTA">
        <jta-data-source>java:/LibrosDS</jta-data-source>

        <class>es.daw.web.entities.Author</class>
        <class>es.daw.web.entities.Book</class>
        
        <properties>
            <property name="jakarta.persistence.schema-generation.database.action" value="none"/>
        </properties>
    </persistence-unit>
</persistence>
```

## 3.Entidades

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

## 4. Uso de las entidades

En base a lo aprendido en clase debes listar productos y salvar el nuevo autor con sus libros.

Observa el siguiente código que realiza la persistencia del autor y sus libros: 

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

No hace falta crear un formulario para especificar nombre de autor y libros.

Deberás cambiar la interface CrudRepository de la siguiente manera:

```
package es.daw.web.repositories;

import java.util.Optional;
import java.util.Set;

import es.daw.web.exceptions.JPAException;

public interface CrudRepository<T> {
    
    Set<T> select() throws JPAException;
    Optional<T> selectById(int id) throws JPAException;
    void deleteById(int id) throws JPAException;
    void save(T t) throws JPAException;

}
```

Implementa lo necesario en base a estas capturas:


![image](https://github.com/user-attachments/assets/3d536a9a-f0dd-4918-9412-688582fc2cb1)

___

![image](https://github.com/user-attachments/assets/9101d3b0-de3a-4779-a7a5-d7f1b6f1c883)

___

Después de dar de alta el nuevo autor y sus libros:

![image](https://github.com/user-attachments/assets/465bda08-307e-4042-be2a-10418887e367)

