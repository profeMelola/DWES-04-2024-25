package es.daw.web.entities;

import java.util.Set;

import jakarta.persistence.*;

@Entity
@Table(name = "categorias")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    //@OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL )
    /*
     * Una instancia de Categoria puede estar asociada con muchos objetos de la clase Producto.
     * Clave foránea (FOREIGN KEY) en la tabla Producto, que apunta a la tabla Categoria.
     * La propiedad mappedBy indica que esta relación es bidireccional.
     * En la entidad Producto, debe haber un atributo llamado categoria que es la contraparte de esta relación.
     * mappedBy dice que productos en Categoria es el lado inverso de la relación y que la clave foránea está gestionada en el lado de Producto
     * cascade = define cómo las operaciones realizadas en la entidad Categoria afectan a las entidades asociadas (en este caso, a los Producto)
     * Al actualizar una categoría, también se actualizan sus productos..... etc
     * orpahnRemoval = true -> si un Producto se elimina de la lista productos en la entidad Categoria,
     * JPA automáticamente eliminará el registro correspondiente de la base de datos
     */
    @OneToMany(mappedBy = "categoria")
    private Set<Producto> productos;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Set<Producto> getProductos() {
        return productos;
    }    
    
    // PARA QUE REALMENTE SEA BIDIRECCIONAL, HAY QUE PERMITIR AÑIDIR PRODUCTOS A LA CATEGORIA
    public void addProducto(Producto p){
        productos.add(p);
        p.setCategoria(this);
    }

    public void removeProducto(Producto p){
        productos.remove(p);
        p.setCategoria(null);
    }

    @Override
    public String toString() {
        return "Categoria [id=" + id + ", nombre=" + nombre + " ] ";
    }



    
}

