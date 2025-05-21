package ing.gpps.entity.pps;


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
    @JoinColumn(name = "proyecto_id")
    private Proyecto proyecto;

    @Column(name = "archivo_url")
    private String archivoUrl;

    @Column(name = "comentarios")
    private String comentarios;

    @Column(name = "tamano_archivo")
    private String tamanoArchivo;

    public Entrega(String titulo, String descripcion, LocalDate fechaLimite, Proyecto proyecto) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fechaLimite = fechaLimite;
        this.proyecto = proyecto;
        this.estado = EstadoEntrega.PENDIENTE;
    }

    public enum EstadoEntrega {
        PENDIENTE,
        ENTREGADO,
        APROBADO,
        RECHAZADO
    }
}