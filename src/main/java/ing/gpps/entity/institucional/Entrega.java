package ing.gpps.entity.institucional;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
public class Entrega {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false)
    private String descripcion;

    @Column(name = "fecha_limite")
    private LocalDate fechaLimite;

    @Column(name = "fecha_entrega")
    private LocalDate fechaEntrega;

    @Enumerated(EnumType.STRING)
    private EstadoEntrega estado;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "fk_numero_actividad", referencedColumnName = "numero"),
            @JoinColumn(name = "fk_numero_planDeTrabajo", referencedColumnName = "fk_numero_planDeTrabajo"),
            @JoinColumn(name = "fk_titulo_proyecto", referencedColumnName = "fk_titulo_proyecto"),
            @JoinColumn(name = "fk_cuit_entidad", referencedColumnName = "fk_cuit_entidad")
    })
    private Actividad actividad;

    @Column(name = "comentarios")
    private String comentarios;

    @Column(name = "fecha")
    private LocalDate fecha;

    @Column(name = "ruta_archivo")
    private String rutaArchivo;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "fk_titulo_proyecto", referencedColumnName = "titulo", insertable = false, updatable = false),
            @JoinColumn(name = "fk_cuit_entidad", referencedColumnName = "cuit", insertable = false, updatable = false)
    })
    private Proyecto proyecto;

    public Entrega(String titulo, String descripcion, LocalDate fechaLimite, Actividad actividad) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fechaLimite = fechaLimite;
        this.actividad = actividad;
        this.estado = EstadoEntrega.PENDIENTE;
    }

    public Entrega() {
        this.estado = EstadoEntrega.PENDIENTE;
    }

    public void setActividad(Actividad actividad) {
        this.actividad = actividad;
    }

    public PlanDeTrabajo getPlanDeTrabajo() {
        return actividad != null ? actividad.getPlanDeTrabajo() : null;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public void setProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
    }

    public void setRutaArchivo(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
    }

    public enum EstadoEntrega {
        PENDIENTE,
        ENTREGADO,
        APROBADO,
        RECHAZADO
    }
}