package es.daw.web.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "cliente_detalle")
public class ClienteDetalle {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    private boolean prime;

    @Column(name = "puntos_acumulados")
    private Long puntosAcumulados;

    @OneToOne
    @JoinColumn(name="cliente_detalle_id", referencedColumnName = "id_cliente")
    private Cliente cliente;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isPrime() {
        return prime;
    }

    public void setPrime(boolean prime) {
        this.prime = prime;
    }

    public Long getPuntosAcumulados() {
        return puntosAcumulados;
    }

    public void setPuntosAcumulados(Long puntosAcumulados) {
        this.puntosAcumulados = puntosAcumulados;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    


    
}
