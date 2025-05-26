package ing.gpps.entity.institucional;

import ing.gpps.entity.idClasses.ActividadId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
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
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(nullable = false)
    private boolean adjuntaArchivo;

    @OneToMany(mappedBy = "actividad", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Informe> informes = new ArrayList<>();

    public Actividad(int numero, String nombre, String descripcion, boolean adjuntaArchivo, PlanDeTrabajo planDeTrabajo) {
        this.actividadId = new ActividadId(numero, planDeTrabajo.planDeTrabajoId());
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.adjuntaArchivo = adjuntaArchivo;
        this.planDeTrabajo = planDeTrabajo;
    }

    protected Actividad() {
    }

    public ActividadId actividadId() {
        return actividadId;
    }

    public void setPlanDeTrabajo(PlanDeTrabajo planDeTrabajo) {
        this.planDeTrabajo = planDeTrabajo;
    }

    // Métodos para manejar informes
    public void addInforme(Informe informe) {
        if (!informes.contains(informe)) {
            informes.add(informe);
            informe.setActividad(this);
        }
    }

    public void removeInforme(Informe informe) {
        if (informes.contains(informe)) {
            informes.remove(informe);
            informe.setActividad(null);
        }
    }

    public List<Informe> getInformes() {
        return new ArrayList<>(informes);
    }
}