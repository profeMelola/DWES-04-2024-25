package es.daw.web.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "facturas")
public class Factura {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Column(name="id_factura")
    private Long id;

    private String descripcion;

    private Long total;

    // ----------------------------------------------------------------------------
    // No se persiste automáticamente la factura si no has asociado el cliente
    //@ManyToOne(fetch = FetchType.LAZY)
    
    // referencedColumnName: permite especificar explícitamente el nombre de la columna en la tabla de la entidad referenciada 
    // que se utilizará como clave foránea.
    // Si no se especifica, JPA asume por defecto que la columna referenciada es la clave primaria de la tabla de la entidad relacionada.    
    // FOREIGN KEY (id_cliente) REFERENCES cliente(id);
    @ManyToOne
    @JoinColumn(name="id_cliente", nullable = false, referencedColumnName = "id_cliente")
    private Cliente cliente;

    // ---------------------------------------------------------------------------------

    // CONSTRUCTOR

    // GETTERS & SETTERS
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
    
   
    
}
