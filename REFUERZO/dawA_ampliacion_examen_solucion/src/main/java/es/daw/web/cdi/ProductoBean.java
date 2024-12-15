package es.daw.web.cdi;

import java.io.Serializable;
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
//@Named
public class ProductoBean implements Serializable{

    @Inject
    CrudRepository<Producto> repoProducto;

    @Inject
    //CrudRepository<Categoria> repoCategoria;
    CrudRepositoryCategoria repoCategoria;

    // Atributo para listar productos cuyo precio > 35. Examen 1ev
    private Set<Producto> productos;

    // Atributo para listar todos lso productos (ampliación del exam 1ev)
    private Set<Producto> productosAll;

    // Atributo para listar las categorías
    private Set<Categoria> categorias;

    // Atributos: parámetros que llegan vía request
    private String nombre;
    private BigDecimal precio;
    private String sku;
    private Long id;

    private Long idCategoria;

    // donde guardar el mensaje del JPAException
    private String mensajeError = "";
    private String descripcionOperacion = "";


    // -----------------------------
    public Set<Producto> getProductos() {
        
        try {
            productos = repoProducto.select();

            System.out.println("***************** LISTA PRODUCTOS PRECIO < 35*************");
            productos.forEach(System.out::println);

            
        } catch (JPAException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return productos;
    }

    
    public Set<Producto> getProductosAll() {
        try {
            productosAll = repoProducto.selectAll();

            System.out.println("***************** LISTA TODOS LOS PRODUCTOS *************");
            productosAll.forEach(System.out::println);

            
        } catch (JPAException e) {
            // TODO Auto-generated catch block
            System.out.println("********** ERROR AL LISTAR TODOS LOS PRODUCTOS **********");
            System.out.println(JpaManagerCdi.getMessageError(e));
        }
        
        return productosAll;
        
    }



    public Set<Categoria> getCategorias() {
        try {
            categorias = repoCategoria.selectAll();
        } catch (JPAException e) {
            //e.printStackTrace();
            System.out.println("********** ERROR AL LISTAR TODAS LAS CATEGORÍAS **********");
            System.out.println(JpaManagerCdi.getMessageError(e));
        } // default para listar todo, sin where
        return categorias;
    }


    // ---------------------------------------------------------------------
    // --------------- MÉTODOS DE ACCIÓN / COMPORTAMIENTO ----------------
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
            //e.printStackTrace();
            System.out.println("********** ERROR AL CREAR EL PRODUCTO **********");
            mensajeError = JpaManagerCdi.getMessageError(e);
            descripcionOperacion = "Error al crear un producto";
            System.err.println(mensajeError);

            // PENDIENTE!!! OTRA FORMA CON MESSAGES DE JSF

            return "error";
        }
        // si se ha persistido el producto, que se muestre la lista
        return "productos?faces-redirect=true";

    }


    public String  mostrarFormularioUpdate(Long idProducto){

        try {
            Optional<Producto> optProducto = repoProducto.selectById(idProducto.intValue());
            //id = optProducto.get().getId();
            setId(optProducto.get().getId());
            setNombre(optProducto.get().getNombre());
            setPrecio(optProducto.get().getPrecio());
            setSku(optProducto.get().getSku());
            setIdCategoria(optProducto.get().getCategoria().getId());
            //idCategoria = optProducto.get().getCategoria().getId();

        } catch (JPAException e) {
            System.out.println("********** ERROR AL CREAR EL PRODUCTO **********");
            descripcionOperacion = "Error al mostrar el formulario de update";
            mensajeError = JpaManagerCdi.getMessageError(e);
            System.err.println(mensajeError);
            return "error";
        }
        return "update";
    }

    public String update(){
        try {
            System.out.println("*********** actualizar producto *****");
            System.out.println("* id:"+id);
            System.out.println("* nombre:"+nombre);
            System.out.println("* precio:"+precio);
            System.out.println("* sku:"+sku);
    
            Producto producto = new Producto();
            producto.setId(id);
            producto.setNombre(nombre);
            producto.setPrecio(precio);
            producto.setSku(sku);

            Optional<Producto> pOptional =repoProducto.selectById(id.intValue());
            producto.setFechaRegistro(pOptional.get().getFechaRegistro());

            // Fase 1: la categoría no se puede actualizar
            //producto.setCategoria(pOptional.get().getCategoria());
            
            // Fase 2: la categoría viene del formulario
            Optional<Categoria> optCat = repoCategoria.selectById(idCategoria.intValue());
            producto.setCategoria(optCat.get());
    
            repoProducto.save(producto);

        } catch (JPAException e) {
            System.out.println("********** ERROR AL ACTUALIZAR EL PRODUCTO **********");
            descripcionOperacion = "Error al actualizar un producto";
            mensajeError = JpaManagerCdi.getMessageError(e);
            System.err.println(mensajeError);
            return "error";
        }
        return "productosAll?faces-redirect=true";
    }

    public String borrar(Long id){
        try {
            repoProducto.deleteById(id.intValue());
        } catch (JPAException e) {
            System.out.println("********** ERROR AL BORRAR UN PRODUCTO **********");
            descripcionOperacion = "Error al borrar un producto";
            mensajeError = JpaManagerCdi.getMessageError(e);
            System.err.println(mensajeError);
            return "error";
        }
        return "productosAll?faces-redirect=true";
    }

    // ----------------------------------------------
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


    public String getDescripcionOperacion() {
        return descripcionOperacion;
    }


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public Long getIdCategoria() {
        return idCategoria;
    }


    public void setIdCategoria(Long idCategoria) {
        this.idCategoria = idCategoria;
    }


    public void setMensajeError(String mensajeError) {
        this.mensajeError = mensajeError;
    }


    public void setDescripcionOperacion(String descripcionOperacion) {
        this.descripcionOperacion = descripcionOperacion;
    }

    
    



    
}
