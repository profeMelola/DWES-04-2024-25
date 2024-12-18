package es.daw.web.cdi;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import es.daw.web.entities.Categoria;
import es.daw.web.entities.Producto;
import es.daw.web.exceptions.JPAException;
import es.daw.web.repositories.CrudRepositoryCategoria;
import es.daw.web.repositories.CrudRepositoryProducto;
import es.daw.web.repositories.JpaManagerCdi;
import jakarta.enterprise.inject.Model;
import jakarta.inject.Inject;

// @RequestScoped
// @Named
@Model
public class ProductoBean {

    // Atributo para listar productos ( precio > 35). Examen 1ev
    private Set<Producto> productos;

    // Atributo para listar todos los productos ( ampliación del Examen 1ev)
    private Set<Producto> productosAll;

    private List<Producto> productosFiltrados;
    private boolean filtrado = false;


    // Atributos para dar de alta un producto
    private String nombre;
    private BigDecimal precio;
    private String sku;

    // Nuevos atributos para la modificación de un producto
    private Long id;
    private Long idCategoria; // pendiente!!!!

    private Set<Categoria> categorias;

    // 
    private String mensajeError = "";

    //private Producto productoActualizar;

    private String descripcionOperacion = "";
    

    @Inject
    //CrudRepository<Producto> repoProducto;
    CrudRepositoryProducto repoProducto;

    @Inject
    CrudRepositoryCategoria repoCategoria;

    // ------------------------------------------------------
    // ------ MÉTODOS DE ACCIÓN, COMPORTAMIENTO -------------
    public Set<Producto> getProductos() {

        try {
            System.out.println("************** FILTRADO ******************");
            System.out.println("filtrado: "+filtrado);
            if(filtrado){
                //return productosFiltrados.stream().collect(Collectors.toSet());
                return new HashSet<>(productosFiltrados);
            }
            productos = repoProducto.select();

        } catch (JPAException e) {
            // TODO Auto-generated catch block
            //e.printStackTrace();
            System.err.println(JpaManagerCdi.getMessageError(e));
        }

        return productos;
    }

    

    public Set<Producto> getProductosAll() {
        // Obtener los productos de la base de datos ( precio > 35)
        try {
            productosAll = repoProducto.selectAll();

        } catch (JPAException e) {
            System.out.println("********* ERROR AL LISTAR TODOS LOS PRODUCTOS ************");
            //e.printStackTrace();
            descripcionOperacion = "Error al listar todos los productos";
            mensajeError = JpaManagerCdi.getMessageError(e);
            System.err.println(mensajeError);

            //return "error"; // PENDIENTE INVESTIGAR!!!!! PARA REDIRIGIR....
        }

        return productosAll;
        
    }

    public Set<Categoria> getCategorias() {

        try {
            categorias=repoCategoria.selectAll();
        } catch (JPAException e) {
            System.out.println("********* ERROR AL LISTAR LAS CATEGORÍAS ************");
            //e.printStackTrace();
            descripcionOperacion = "Error al listar todos los productos";
            mensajeError = JpaManagerCdi.getMessageError(e);
            System.err.println(mensajeError);
        }
        return categorias;
    }


    // ---------------------------------------------------------
    /**
     * persistir (dar de alta un producto. Examen 1ev)
     * @return
     */
    public String persistir(){

        Producto producto = new Producto();
        producto.setNombre(nombre);
        producto.setPrecio(precio);
        producto.setSku(sku);

        // pendiente pintar por consola los parámetros

        // necesito un objeto categoría!!!!!
        try {
            Optional<Categoria> categoria = repoCategoria.selectById(2L);

            // 2 FORMAS
            // FORMA 1
            producto.setCategoria(categoria.get());
            // FORMA 2
            //categoria.get().addProducto(producto);

            repoProducto.save(producto);

            /*
             * JPA automáticamente sincroniza la relación inversa (la lista de productos en la categoría) si ambas entidades están siendo gestionadas 
             * por el contexto de persistencia (es decir, están "attached" o son entidades persistidas). 
             * Esto es porque el contexto de persistencia garantiza la coherencia entre los lados de la relación.
             * Sin embargo, usar métodos como addProducto explícitamente es una buena práctica porque garantizas que la relación se mantenga coherente incluso fuera del contexto de persistencia.
             */
            // PINTAR PRODUCTOS CUYA CATEGORÍA ES ROPA (2)
            System.out.println("\n********** LISTADO DE PRODUCTOS CON CATEGORÍA ROPA ***********");
            categoria.get().getProductos().forEach(System.out::println);

        } catch (JPAException e) {
            System.out.println("********* ERROR AL CREAR PRODUCTO ************");
            e.printStackTrace();
            descripcionOperacion = "Error al dar de alta un producto producto";
            mensajeError = JpaManagerCdi.getMessageError(e);
            System.err.println(mensajeError);

            // PENDIENTE!!! OTRA FORMA CON MESSAGES DE JSF
            // cuidadín!!!! no usar faces-redirect=true porque no se pintarán los mensajes...

            return "error";
        }
        return "productos?faces-redirect=true";


    }

    /**
     * mostrarFormularioUpdate (ampliación examen 1ev)
     * @param p
     * @return
     */
    public String mostrarFormularioUpdate(Long idProducto){

        System.out.println("********** FORMULARIO DE ACTUALIZACIÓN ****************");

        try {
            Optional<Producto> optProducto = repoProducto.selectById(idProducto);

            // Cargo en el atributo el producto a actualizar
            //setProductoActualizar(optProducto.get()); //candidato a cargármelo!!!!!
            //this.productoActualizar = optProducto.get(); // es lo mismo que setProductoActualizar
            //System.out.println("*** producto a actualizar: "+p);
            //
            setId(optProducto.get().getId());
            setIdCategoria(optProducto.get().getCategoria().getId());
            setSku(optProducto.get().getSku());
            setNombre(optProducto.get().getNombre());
            setPrecio(optProducto.get().getPrecio());

            

        } catch (JPAException e) {
            System.out.println("********* ERROR AL mostrarFormularioUpdate ************");
            //e.printStackTrace();
            descripcionOperacion = "Error al mostrar el formulario de edición del producto";
            mensajeError = JpaManagerCdi.getMessageError(e);
            System.err.println(mensajeError);
            return "error";
        }

        //return "update?faces-redirect=true"; // cambiar la URL del navegador, pero pierdo los datos del bean producto
        return "update"; //update.xhtml es el facelet para modificar un producto. No cambia la URL
    }

    /**
     * update del producto (ampliación examen 1ev)
     * @return
     */
    public String update(){
        
        try {
            System.out.println("********** actualizar producto *************");
            //System.out.println("* productoActualizar: "+ productoActualizar);
    
            System.out.println("* id: "+id);
            System.out.println("* nombre: "+nombre);
            System.out.println("* precio: "+precio);
            System.out.println("* sku: "+sku);
    
            // Pendiente crear un nuevo objeto producto con los datos recogidos del formulario de edición
            Producto producto = new Producto(); 
            producto.setId(id); //hidden
            producto.setNombre(nombre); //text
            producto.setPrecio(precio);
            producto.setSku(sku);

            
            // y fecha de registro???
            Optional<Producto> optProducto = repoProducto.selectById(id);
            producto.setFechaRegistro(optProducto.get().getFechaRegistro());

            // y categoría?
            // Caso 1: os digo que la categoría no se puede actualizar.
            //producto.setCategoria(optProducto.get().getCategoria());

            // PENDIENTE SIMULACRO DE EXAMEN!!!!!
            // Caso 2: debe poderse editar la categoría

            // buscar el opt categoría por id
            Optional<Categoria> optCat = repoCategoria.selectById(idCategoria);
            producto.setCategoria(optCat.get());
            
            // pendiente controlar en el save si es persist o merge...
            repoProducto.save(producto);

        } catch (JPAException e) {
            System.out.println("********* ERROR AL ACTUALIZAR EL PRODUCTO ************");
            //e.printStackTrace();
            descripcionOperacion = "Error al actualizar el producto";
            mensajeError = JpaManagerCdi.getMessageError(e);
            System.err.println(mensajeError);
            return "error";
        }

        return "productosAll?faces-redirect=true";
    }
    // ---------- getters & setters -----------    
    // public void setProductos(Set<Producto> productos) {
    //     this.productos = productos;
    // }


    public String borrar(Long id){
        // ????
        try {
            repoProducto.deleteById(id);
        } catch (JPAException e) {
            System.out.println("********* ERROR AL BORRAR EL PRODUCTO ************");
            //e.printStackTrace();
            descripcionOperacion = "Error al borrar el producto";
            mensajeError = JpaManagerCdi.getMessageError(e);
            System.err.println(mensajeError);
            return "error";
        }
        return "productosAll?faces-redirect=true";

    }


    /**
     * 
     * @return
     */
    public String filtrar(){
        System.out.println("***************************************************");
        System.out.println("[FILTRAR] ID DE CATEGORÍA A FILTRAR: "+idCategoria);
        System.out.println("***************************************************");

        try {
            productos = repoProducto.selectAll();

            productosFiltrados = productos.stream()
                        .filter(p -> p.getCategoria().getId() == idCategoria).collect(Collectors.toList());


            System.out.println("************************");
            productosFiltrados.forEach(System.out::println);
            System.out.println("************************");
           

        } catch (JPAException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // FORMA 1: sin reutilizar la vista productos.xhtml
        //return "productosFiltrados";

        // FORMA 2: reutilizando la vista productos.xhtml
        // La variable boolean "filtrado" permite que el método getProductos() devuelva la lista de productos filtrados o sin filtrar por categoría
        filtrado = true;
        return "productos";
    }

    // ----------
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

    // public Producto getProductoActualizar() {
    //     return productoActualizar;
    // }

    // public void setProductoActualizar(Producto productoActualizar) {
    //     this.productoActualizar = productoActualizar;
    // }

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

    public String getDescripcionOperacion() {
        return descripcionOperacion;
    }



    public void setMensajeError(String mensajeError) {
        this.mensajeError = mensajeError;
    }



    public void setDescripcionOperacion(String descripcionOperacion) {
        this.descripcionOperacion = descripcionOperacion;
    }



    // /**
    //  * getProductosFiltrados
    //  * @return
    //  */
    // public List<Producto> getProductosFiltrados(Long idCategoria) {
    //     System.out.println("**************************************************");
    //     System.out.println("************ GET PRODUCTOS FILTRADOS *************");
    //     System.out.println("**************************************************");
    //     System.out.println("[FILTRAR] ID DE CATEGORÍA A FILTRAR: "+idCategoria);
    //     System.out.println("**************************************************");


    //     // lista de todos los productos
    //     try {
    //         productos = repoProducto.selectAll();

    //         productosFiltrados = productos.stream()
    //                     .filter(p -> p.getCategoria().getId() == idCategoria).collect(Collectors.toList());


    //         System.out.println("************************");
    //         productosFiltrados.forEach(System.out::println);
    //         System.out.println("************************");

    //         //setProductosFiltrados(productosFiltrados);

    //     } catch (JPAException e) {
    //         // TODO Auto-generated catch block
    //         e.printStackTrace();
    //     }
    //     // filtrar productos con id categía

    //     return productosFiltrados;
    // }

    public List<Producto> getProductosFiltrados() {
        System.out.println("**************************************************");
        System.out.println("************ GET PRODUCTOS FILTRADOS *************");
        System.out.println("**************************************************");
        productosFiltrados.forEach(System.out::println);

        return productosFiltrados;
    }

    
}
