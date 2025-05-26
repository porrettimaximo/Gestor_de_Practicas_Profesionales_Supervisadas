package ing.gpps.entity.institucional;

import ing.gpps.entity.idClasses.InformeId;
import ing.gpps.entity.users.Estudiante;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Informe {

    @EmbeddedId
    private InformeId id;

    @ManyToOne
    @MapsId("estudianteDni")
    @JoinColumn(name = "fk_id_estudiante")
    private Estudiante estudiante;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "fk_numero_actividad", referencedColumnName = "numero"),
            @JoinColumn(name = "fk_titulo_proyecto", referencedColumnName = "fk_titulo_proyecto"),
            @JoinColumn(name = "fk_cuit_entidad", referencedColumnName = "fk_cuit_entidad"),
            @JoinColumn(name = "fk_numero_planDeTrabajo", referencedColumnName = "fk_numero_planDeTrabajo")
    })
    private Actividad actividad;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false)
    private String ruta; // enlace de Drive

    public Informe(int numero, LocalDate fecha, String titulo, String ruta, Estudiante estudiante, Actividad actividad) {
        this.id = new InformeId(numero, estudiante.getDni());
        this.fecha = fecha;
        this.titulo = titulo;
        this.ruta = ruta;
        this.estudiante = estudiante;
        this.actividad = actividad;
    }

    protected Informe() {
    }

    public void setActividad(Actividad actividad) {
        this.actividad = actividad;
    }
}

