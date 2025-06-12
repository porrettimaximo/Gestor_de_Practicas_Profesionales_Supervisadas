package ing.gpps.entity.institucional;

import ing.gpps.entity.idClasses.InformeId;
import ing.gpps.entity.users.Estudiante;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
public class Informe {

    @EmbeddedId
    private InformeId id;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("estudianteDni")
    @JoinColumn(name = "fk_id_estudiante")
    private Estudiante estudiante;

    @ManyToOne(fetch = FetchType.EAGER)
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

    @Column(nullable = true)
    private String descripcion;

    @Column(name = "ruta_archivo", nullable = true)
    private String rutaArchivo;

    public Informe(int numero, LocalDate fecha, String titulo, String descripcion, String rutaArchivo, Estudiante estudiante, Actividad actividad) {
        if (estudiante == null || estudiante.getDni() == null) {
            throw new IllegalArgumentException("El estudiante y su DNI no pueden ser nulos");
        }
        this.id = new InformeId();
        this.id.setNumero(numero);
        this.id.setEstudianteDni(estudiante.getDni().intValue());
        this.fecha = fecha;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.rutaArchivo = rutaArchivo;
        this.estudiante = estudiante;
        this.actividad = actividad;
    }

    protected Informe() {
    }

    public void setActividad(Actividad actividad) {
        this.actividad = actividad;
    }
}

