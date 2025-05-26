package ing.gpps.entity.institucional;

import jakarta.persistence.*;
import lombok.Getter;
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

    // CAMBIO PRINCIPAL: Ahora se asocia a Actividad en lugar de PlanDeTrabajo
    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "fk_numero_actividad", referencedColumnName = "numero"),
            @JoinColumn(name = "fk_numero_planDeTrabajo", referencedColumnName = "fk_numero_planDeTrabajo"),
            @JoinColumn(name = "fk_titulo_proyecto", referencedColumnName = "fk_titulo_proyecto"),
            @JoinColumn(name = "fk_cuit_entidad", referencedColumnName = "fk_cuit_entidad")
    })
    private Actividad actividad;

    @Column(name = "archivo_url")
    private String archivoUrl;

    @Column(name = "comentarios")
    private String comentarios;

    @Column(name = "tamano_archivo")
    private String tamanoArchivo;

    public Entrega(String titulo, String descripcion, LocalDate fechaLimite, Actividad actividad) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fechaLimite = fechaLimite;
        this.actividad = actividad;
        this.estado = EstadoEntrega.PENDIENTE;
    }

    protected Entrega() {
    }

    public void setActividad(Actividad actividad) {
        this.actividad = actividad;
    }

    // Método de conveniencia para acceder al PlanDeTrabajo a través de la actividad
    public PlanDeTrabajo getPlanDeTrabajo() {
        return actividad != null ? actividad.getPlanDeTrabajo() : null;
    }

    public enum EstadoEntrega {
        PENDIENTE,
        ENTREGADO,
        APROBADO,
        RECHAZADO
    }
}