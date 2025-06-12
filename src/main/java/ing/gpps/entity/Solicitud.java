package ing.gpps.entity;

import ing.gpps.entity.institucional.Proyecto;
import ing.gpps.entity.users.Estudiante;
import ing.gpps.entity.users.Usuario;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Solicitud {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "solicitante_id", nullable = false)
    private Estudiante solicitante;

    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "titulo_proyecto", referencedColumnName = "titulo"),
        @JoinColumn(name = "cuit_entidad", referencedColumnName = "cuit")
    })
    private Proyecto proyecto;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EstadoSolicitud estado;

    @Column(nullable = false)
    private LocalDateTime fechaSolicitud;

    public enum EstadoSolicitud {
        PENDIENTE,
        APROBADA,
        RECHAZADA
    }

    public Solicitud(Estudiante solicitante, Proyecto proyecto) {
        this.solicitante = solicitante;
        this.proyecto = proyecto;
        this.estado = EstadoSolicitud.PENDIENTE;
        this.fechaSolicitud = LocalDateTime.now();
    }

    public void aprobar() {
        this.estado = EstadoSolicitud.APROBADA;
    }

    public void rechazar() {
        this.estado = EstadoSolicitud.RECHAZADA;
    }

    public boolean estaPendiente() {
        return estado == EstadoSolicitud.PENDIENTE;
    }

    public boolean estaAprobada() {
        return estado == EstadoSolicitud.APROBADA;
    }

    public boolean estaRechazada() {
        return estado == EstadoSolicitud.RECHAZADA;
    }
}
