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

    /*
     Una instancia de Author puede estar asociada con múltiples instancias de Book.
     Un autor tiene una lista de libros (en este ejemplo un conjunto). 
  
     En la clase Book, hay un campo que se llama author, que representa la relación con el autor (private Author author;).
     Al definir mappedBy, le estamos diciendo a JPA que Book tiene la clave foránea author_id. 
    

     // El Cascade se maneja en el entity principal o padre
     CascadeType.ALL: permite que cualquier operación (persistir, eliminar, actualizar) realizada en un autor se aplique también en sus libros.
     orphanRemoval = true: elimina de la base de datos cualquier libro que se elimine de la lista books del autor.

    */

     @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Book> books;

  ....
```
