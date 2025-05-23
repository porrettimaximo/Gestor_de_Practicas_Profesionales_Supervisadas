package ing.gpps.entity.institucional;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
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
            @JoinColumn(name = "fk_numero_planDeTrabajo", referencedColumnName = "numero"),
            @JoinColumn(name = "fk_titulo_proyecto", referencedColumnName = "titulo_proyecto"),
            @JoinColumn(name = "fk_cuit_entidad", referencedColumnName = "cuit_entidad")
    })
    private PlanDeTrabajo planDeTrabajo;

    @Column(name = "archivo_url")
    private String archivoUrl;

    @Column(name = "comentarios")
    private String comentarios;

    @Column(name = "tamano_archivo")
    private String tamanoArchivo;

    public Entrega(String titulo, String descripcion, LocalDate fechaLimite, PlanDeTrabajo planDeTrabajo) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fechaLimite = fechaLimite;
        this.planDeTrabajo = planDeTrabajo;
        this.estado = EstadoEntrega.PENDIENTE;
    }

    public void setPlanDeTrabajo(PlanDeTrabajo planDeTrabajo) {
        this.planDeTrabajo = planDeTrabajo;
    }

    public enum EstadoEntrega {
        PENDIENTE,
        ENTREGADO,
        APROBADO,
        RECHAZADO
    }
}