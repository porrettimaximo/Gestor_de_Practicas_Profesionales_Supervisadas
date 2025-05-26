package ing.gpps.entity.idClasses;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ActividadId implements Serializable {

    @Column(name = "numero")
    private int numero;

    @Embedded
    private PlanDeTrabajoId planDeTrabajoId;

    public ActividadId() {
    }

    public ActividadId(int numero, PlanDeTrabajoId planDeTrabajoId) {
        this.numero = numero;
        this.planDeTrabajoId = planDeTrabajoId;
    }

    public int numero() {
        return numero;
    }

    public PlanDeTrabajoId planDeTrabajoId() {
        return planDeTrabajoId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ActividadId that)) return false;
        return numero == that.numero && planDeTrabajoId.equals(that.planDeTrabajoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numero, planDeTrabajoId);
    }
}