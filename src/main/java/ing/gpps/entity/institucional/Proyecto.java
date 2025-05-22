package ing.gpps.entity.institucional;

import ing.gpps.entity.idClasses.ProyectoId;
import ing.gpps.entity.users.Estudiante;
import ing.gpps.entity.users.TutorExterno;
import ing.gpps.entity.users.Usuario;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Proyecto {

    @EmbeddedId
    private ProyectoId proyectoId;

    @ManyToOne
    @MapsId("cuitEntidad")
    @JoinColumn(name = "cuit")
    private Entidad entidad;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "fecha_inicio")
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin_estimada")
    private LocalDate fechaFinEstimada;

    @ManyToOne
    @JoinColumn(name = "fk_nombre_area")
    private Area area;

    @OneToOne(mappedBy = "proyecto")
    private PlanDeTrabajo planDeTrabajo;

    @ManyToOne
    @JoinColumn(name = "estudiante_id")
    private Estudiante estudiante;

    @ManyToOne
    @JoinColumn(name = "tutor_unrn_id")
    private Usuario tutorUNRN;

    @ManyToOne
    @JoinColumn(name = "tutor_externo_id")
    private TutorExterno tutorExterno;

    @OneToMany(mappedBy = "proyecto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Entrega> entregas = new ArrayList<>();

    @ElementCollection
    private List<String> objetivos = new ArrayList<>();

    @Column(name = "progreso")
    private int progreso;

    public Proyecto(String titulo, String descripcion, LocalDate fechaInicio, LocalDate fechaFinEstimada,
                    Estudiante estudiante, Usuario tutorUNRN, TutorExterno tutorExterno, Entidad entidad) {
        this.descripcion = descripcion;
        this.fechaInicio = fechaInicio;
        this.fechaFinEstimada = fechaFinEstimada;
        this.estudiante = estudiante;
        this.tutorUNRN = tutorUNRN;
        this.tutorExterno = tutorExterno;
        this.entidad = entidad;
        this.progreso = 0;
        this.proyectoId = new ProyectoId(titulo, entidad.cuit());
    }

    public void addObjetivo(String objetivo) {
        this.objetivos.add(objetivo);
    }

    public void addEntrega(Entrega entrega) {
        entregas.add(entrega);
        entrega.setProyecto(this);
    }

    public void removeEntrega(Entrega entrega) {
        entregas.remove(entrega);
        entrega.setProyecto(null);
    }

    // Getters
    public ProyectoId proyectoId() {
        return proyectoId;
    }

    public Entidad entidad() {
        return entidad;
    }

    public String getTitulo() {
        return proyectoId != null ? proyectoId.titulo() : null;
    }
}
