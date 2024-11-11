package es.daw.web.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import java.time.LocalDateTime;

/*
 * No se mapea como una tabla independiente en la base de datos. 
 * En cambio, sus atributos se integran en la tabla de la entidad que la contiene.
 */
@Embeddable
public class Auditoria {

    @Column(name = "creado_en")
    private LocalDateTime creadoEn;

    @Column(name = "editado_en")
    private LocalDateTime editadoEn;

    @PrePersist
    public void prePersist() {
        System.out.println("inicializar algo justo antes del persist");
        this.creadoEn = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        System.out.println("inicializar algo justo antes del update");
        this.editadoEn = LocalDateTime.now();
    }

    public LocalDateTime getCreadoEn() {
        return creadoEn;
    }

    public void setCreadoEn(LocalDateTime creadoEn) {
        this.creadoEn = creadoEn;
    }

    public LocalDateTime getEditadoEn() {
        return editadoEn;
    }

    public void setEditadoEn(LocalDateTime editadoEn) {
        this.editadoEn = editadoEn;
    }

}

