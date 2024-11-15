package es.daw.web.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name ="clientes")
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "id_cliente")
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String apellido;

    @Enumerated(EnumType.STRING)
    @Column(name = "forma_pago")
    private FormaPago formaPago;

    @Embedded
    private Auditoria audit;

    // orphanRemoval: Elimina de la tabla Direcciones, cualquier dirección que se elimine de la lista direcciones del Cliente
    // Si no especificas la clave foránea (mapped) crea la tabla intermedia (clientes_direcciones)
    // @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true) //Al no existir la tabla, asume que es una relación M2M

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Direccion> direcciones;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "cliente")
    private ClienteDetalle detalle;


    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "cliente")
    private List<Factura> facturas;

    /*
     * EXPLICACIÓN DE CascadeType y orphanRemoval EN LOS EJEMPLOS ANTERIORES
     * La propiedad cascade define cómo se propagan las operaciones realizadas en una entidad a las entidades relacionadas. 
     * Los valores comunes en CascadeType incluyen:

            CascadeType.PERSIST: Cuando se persiste la entidad principal, se persisten también las entidades relacionadas.
            CascadeType.MERGE: Cuando se actualiza la entidad principal, también se actualizan las entidades relacionadas.
            CascadeType.REMOVE: Cuando se elimina la entidad principal, se eliminan también las entidades relacionadas.
            CascadeType.REFRESH: Refresca las entidades relacionadas cuando se refresca la principal.
            CascadeType.ALL: Aplica todos los tipos de cascada (PERSIST, MERGE, REMOVE, REFRESH, DETACH).

     *
     * La propiedad orphanRemoval está relacionada con la gestión de entidades relacionadas que quedan "huérfanas". 
     * Una entidad se considera "huérfana" si ya no está asociada a su entidad padre en la relación.
     * orphanRemoval = true, elimina de la base de datos las entidades relacionadas que ya no están asociadas a su padre.
     * 
     * En nuestros ejemplos:
     * - Si quitas una Direccion o Factura de la lista de direcciones o facturas de un Cliente, JPA eliminará automáticamente esa dirección o factura de la base de datos
     * - Si detalle en un Cliente se establece como null, JPA eliminará automáticamente el registro de ClienteDetalle correspondiente de la base de datos
     */



    // CONSTRUCTOR
    public Cliente(){
        direcciones = new ArrayList<>();
        audit = new Auditoria();
        facturas = new ArrayList<>();
    }

    // GETTERS AND SETTERS

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

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public FormaPago getFormaPago() {
        return formaPago;
    }

    public void setFormaPago(FormaPago formaPago) {
        this.formaPago = formaPago;
    }

    public Auditoria getAudit() {
        return audit;
    }

    public void setAudit(Auditoria audit) {
        this.audit = audit;
    }

    public List<Direccion> getDirecciones() {
        return direcciones;
    }

    public void setDirecciones(List<Direccion> direcciones) {
        this.direcciones = direcciones;
    }


    public ClienteDetalle getDetalle() {
        return detalle;
    }

    public void setDetalle(ClienteDetalle detalle) {
        this.detalle = detalle;
        detalle.setCliente(this);
    }

    
    // -------
    public void addDireccion(Direccion direccion){
        direcciones.add(direccion);
        direccion.setCliente(this);
    }

    public void removeDireccion(Direccion direccion){
        direcciones.remove(direccion);
        direccion.setCliente(null);
    }


    public void addFactura(Factura factura){
        facturas.add(factura);
        factura.setCliente(this);
    }

    public void removeFactura(Factura factura){
        facturas.remove(factura);
        factura.setCliente(null);
    }

}
