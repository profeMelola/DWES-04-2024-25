package es.daw.web.entities;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="fabricante")
public class Fabricante implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //delega la generación de la primary ke a la bd (autoincrement)
    @Column(name="codigo")
    private int codigo;

    @Column(name="nombre")
    private String nombre;

    

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return "Fabricante{" + "codigo=" + codigo + ", nombre=" + nombre + '}';
    }

}

