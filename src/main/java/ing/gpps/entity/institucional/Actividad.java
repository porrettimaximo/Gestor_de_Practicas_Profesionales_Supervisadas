package ing.gpps.entity.institucional;

import ing.gpps.entity.idClasses.ActividadId;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Actividad {

    @EmbeddedId
    private ActividadId actividadId;

    @ManyToOne
    @MapsId("planDeTrabajoId")
    @JoinColumns({
            @JoinColumn(name = "fk_numero_planDeTrabajo", referencedColumnName = "numero"),
            @JoinColumn(name = "fk_titulo_proyecto", referencedColumnName = "titulo_proyecto"),
            @JoinColumn(name = "fk_cuit_entidad", referencedColumnName = "cuit_entidad")
    })
    private PlanDeTrabajo planDeTrabajo;

    @Column(nullable = false)
    private boolean adjuntaArchivo;

    @OneToMany(mappedBy = "actividad", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Informe> informes = new ArrayList<>();

    public Actividad(int numero, boolean adjuntaArchivo, PlanDeTrabajo planDeTrabajo) {
        this.actividadId = new ActividadId(numero, planDeTrabajo.planDeTrabajoId());
        this.adjuntaArchivo = adjuntaArchivo;
        this.planDeTrabajo = planDeTrabajo;
    }

    protected Actividad() {
    }
}

