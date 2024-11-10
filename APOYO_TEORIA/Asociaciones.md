# @OneToMany + @ManyToOne

![image](https://github.com/user-attachments/assets/b2ecf836-0ae3-43ad-9bfd-3748102c72c8) ![image](https://github.com/user-attachments/assets/89481a6a-4d66-4c38-841d-8b95c3fee2f9)


## Entidad propietaria Author: @OneToMany

```
@Entity
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Book> books;

  ....
```

- Una instancia de Author puede estar asociada con múltiples instancias de Book.
- Un autor tiene una lista de libros (en este ejemplo un conjunto).
  
- En la clase Book, hay un campo que se llama author, que representa la relación con el autor (private Author author;).
- Al definir **mappedBy**:
    - Le estamos diciendo a JPA que Book tiene la clave foránea author_id.
    - Tiene el valor de author, que es el nombre del campo en Book.
  
- El **Cascade** se maneja en el entity principal o padre:
    - **CascadeType.ALL:** permite que cualquier operación (persistir, eliminar, actualizar) realizada en un autor se aplique también en sus libros.
    - **orphanRemoval = true:** elimina de la base de datos cualquier libro que se elimine de la lista books del autor.


## Entidad poseída Book: @ManyTOne

```
@Entity
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    // Muchos libros pueden tener el mismo autor....
    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;

    @Temporal(TemporalType.DATE)
    @Column(name="publication_date")
    private LocalDate publicationDate;
```
- Muchos libros (Book) pueden estar asociados con un único autor (Author).
- La anotación **@JoinColumn** se utiliza para especificar la columna en la tabla Book que se usará como clave foránea para referenciar al Author.
    - El atributo **name = "author_id"** indica que esta columna en la tabla Book se llamará author_id.
