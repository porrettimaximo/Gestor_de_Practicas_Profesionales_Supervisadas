package ing.gpps.entity.idClasses;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class PlanDeTrabajoId implements Serializable {

    @Column(name = "numero")
    private int numero;

    @Embedded
    private ProyectoId proyectoId;

    public PlanDeTrabajoId() {
    }

    public PlanDeTrabajoId(int numero, ProyectoId proyectoId) {
        this.numero = numero;
        this.proyectoId = proyectoId;
    }

    public int numero() {
        return numero;
    }

    public ProyectoId proyectoId() {
        return proyectoId;
    }

    public String tituloProyecto() {
        return proyectoId.titulo();
    }

    public Long cuitEntidad() {
        return proyectoId.cuitEntidad();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PlanDeTrabajoId that)) return false;
        return numero == that.numero && proyectoId.equals(that.proyectoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numero, proyectoId);
    }
}