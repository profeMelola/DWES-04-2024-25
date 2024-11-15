package es.daw.web.entities;

import java.io.Serializable;

import jakarta.persistence.*;

@Entity
@Table(name="direcciones")
public class Direccion implements Serializable{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String calle;
    private Integer numero;

    // Para evitar la tabla intermedia
    // @JoinColumn crea la clave foránea que hace referencia a Cliente
    @ManyToOne
    @JoinColumn(name="cliente_id", nullable = false)
    private Cliente cliente;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getCalle() {
        return calle;
    }
    public void setCalle(String calle) {
        this.calle = calle;
    }
    public Integer getNumero() {
        return numero;
    }
    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    // ----------- añado getters and setters de cliente ----------
    public Cliente getCliente() {
        return cliente;
    }
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }    

    @Override
    public String toString() {
        return "Direccion [id=" + id + ", calle=" + calle + ", numero=" + numero + "]";
    }


    

}
