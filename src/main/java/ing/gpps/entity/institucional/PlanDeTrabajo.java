package ing.gpps.entity.institucional;

import ing.gpps.entity.idClasses.PlanDeTrabajoId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class PlanDeTrabajo {

    @EmbeddedId
    private PlanDeTrabajoId planDeTrabajoId;

    @OneToOne
    @MapsId("proyectoId")
    @JoinColumns({
            @JoinColumn(name = "titulo_proyecto", referencedColumnName = "titulo"),
            @JoinColumn(name = "cuit_entidad", referencedColumnName = "cuit")
    })
    private Proyecto proyecto;

    @Column(nullable = false)
    private LocalDate fechaInicio;

    @Column(nullable = false)
    private LocalDate fechaFin;

    @OneToMany(mappedBy = "planDeTrabajo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Actividad> actividades = new ArrayList<>();

    @OneToMany(mappedBy = "planDeTrabajo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Entrega> entregas = new ArrayList<>();

    public PlanDeTrabajo(int numero, LocalDate fechaInicio, LocalDate fechaFin, Proyecto proyecto) {
        this.planDeTrabajoId = new PlanDeTrabajoId(numero, proyecto.getProyectoId());
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.proyecto = proyecto;

        // Establecer la relación bidireccional
        if (proyecto.getPlanDeTrabajo() != this) {
            proyecto.setPlanDeTrabajo(this);
        }
    }

    protected PlanDeTrabajo() {
    }

    public PlanDeTrabajoId planDeTrabajoId() {
        return planDeTrabajoId;
    }

    // Métodos para manejar entregas
    public void addEntrega(Entrega entrega) {
        if (!entregas.contains(entrega)) {
            entregas.add(entrega);
            entrega.setPlanDeTrabajo(this);
        }
    }

    public void removeEntrega(Entrega entrega) {
        if (entregas.contains(entrega)) {
            entregas.remove(entrega);
            entrega.setPlanDeTrabajo(null);
        }
    }

    public List<Entrega> getEntregas() {
        return new ArrayList<>(entregas);
    }

    // Métodos para manejar actividades
    public void addActividad(Actividad actividad) {
        if (!actividades.contains(actividad)) {
            actividades.add(actividad);
            actividad.setPlanDeTrabajo(this);
        }
    }

    public void removeActividad(Actividad actividad) {
        if (actividades.contains(actividad)) {
            actividades.remove(actividad);
            actividad.setPlanDeTrabajo(null);
        }
    }

    public List<Actividad> getActividades() {
        return new ArrayList<>(actividades);
    }

    public void setProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
        if (proyecto != null && proyecto.getPlanDeTrabajo() != this) {
            proyecto.setPlanDeTrabajo(this);
        }
    }
}