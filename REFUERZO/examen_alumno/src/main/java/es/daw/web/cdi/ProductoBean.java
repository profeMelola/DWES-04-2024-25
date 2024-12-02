package es.daw.web.cdi;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;

import es.daw.web.entities.Categoria;
import es.daw.web.entities.Producto;
import es.daw.web.exceptions.JPAException;
import es.daw.web.repositories.CrudRepository;
import es.daw.web.repositories.CrudRepositoryCategoria;
import es.daw.web.repositories.JpaManagerCdi;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.Model;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Model
//@RequestScoped
//@Named("miBean")
public class ProductoBean {

    @Inject
    CrudRepository<Producto> repoProducto;

    @Inject
    //CrudRepository<Categoria> repoCategoria;
    CrudRepositoryCategoria repoCategoria;

    Set<Producto> productos;

    // Atritubos: parámetros que llegan vía request
    private String nombre;
    private BigDecimal precio;
    private String sku;

    // donde guardar el mensaje del JPAException
    private String mensajeError = "";

    public Set<Producto> getProductos() {
        
        try {
            productos = repoProducto.select();

            System.out.println("***************** LISTA PRODUCTOS *************");
            productos.forEach(System.out::println);

            
        } catch (JPAException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return productos;
    }


    public String persistir(){
        // Crear un producto

        Producto producto = new Producto();
        producto.setNombre(nombre);
        producto.setPrecio(precio);
        producto.setSku(sku);
        
        
        try {
            // Obtener el objeto categoría (registro en BD) cuyo id es el 2
            // Debería venir el id de categoría de la vista (opción elegida por el usuario en el formulario)            
            Optional<Categoria> categoria = repoCategoria.selectById(2);

            // DAR DE ALTA PRODUCTO: 2 FORMAS
            // FORMA 1
            //producto.setCategoria(categoria.get());

            // FORMA 2
            categoria.get().addProducto(producto);

            repoProducto.save(producto);

            /*
             * JPA automáticamente sincroniza la relación inversa (la lista de productos en la categoría) si ambas entidades están siendo gestionadas 
             * por el contexto de persistencia (es decir, están "attached" o son entidades persistidas). 
             * Esto es porque el contexto de persistencia garantiza la coherencia entre los lados de la relación.
             * Sin embargo, usar métodos como addProducto explícitamente es una buena práctica porque garantizas que la relación se mantenga coherente incluso fuera del contexto de persistencia.
             */

            System.out.println("\n**************** LISTADO DE PRODUCTOS CON CATEGORÍA ROPA ********************");
            categoria.get().getProductos().forEach(System.out::println);

        } catch (JPAException e) {
            // SI SE PRODUCE UN ERROR!!! PENDIENTE
            //e.printStackTrace();
            System.out.println("********** ERROR AL CREAR EL PRODUCTO **********");
            mensajeError = JpaManagerCdi.getMessageError(e);
            System.err.println(mensajeError);

            // PENDIENTE!!! OTRA FORMA CON MESSAGES DE JSF

            return "error";
        }
        // si se ha persistido el producto, que se muestre la lista
        return "productos?faces-redirect=true";

    }

    // Getters & Setters
    public String getNombre() {
        return nombre;
    }


    public void setNombre(String nombre) {
        this.nombre = nombre;
    }


    public BigDecimal getPrecio() {
        return precio;
    }


    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }


    public String getSku() {
        return sku;
    }


    public void setSku(String sku) {
        this.sku = sku;
    }


    public String getMensajeError() {
        return mensajeError;
    }

    




    
}
