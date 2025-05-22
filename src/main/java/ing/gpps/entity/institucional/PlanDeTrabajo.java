package ing.gpps.entity.institucional;

import ing.gpps.entity.idClasses.PlanDeTrabajoId;
import ing.gpps.entity.users.DireccionDeCarrera;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class PlanDeTrabajo {

    @EmbeddedId
    private PlanDeTrabajoId planDeTrabajoId;

    @ManyToOne
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

    @ManyToOne
    @JoinColumn(name = "fk_id_direccion_de_carrera")
    private DireccionDeCarrera direccionDeCarrera;

    @OneToMany(mappedBy = "planDeTrabajo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Actividad> actividades;

    public PlanDeTrabajo(int numero, LocalDate inicio, LocalDate fin, DireccionDeCarrera dir, Proyecto proyecto) {
        this.planDeTrabajoId = new PlanDeTrabajoId(numero, proyecto.proyectoId());
        this.fechaInicio = inicio;
        this.fechaFin = fin;
        this.direccionDeCarrera = dir;
        this.proyecto = proyecto;
    }

    protected PlanDeTrabajo() {
    }

    public PlanDeTrabajoId planDeTrabajoId() {
        return planDeTrabajoId;
    }
}

