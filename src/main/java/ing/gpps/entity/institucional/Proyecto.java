package ing.gpps.entity.institucional;

import ing.gpps.entity.idClasses.ProyectoId;
import ing.gpps.entity.users.Estudiante;
import ing.gpps.entity.users.TutorExterno;
import ing.gpps.entity.users.Usuario;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @ManyToOne
    @JoinColumn(name = "fk_nombre_area")
    private Area area;

    @OneToOne(mappedBy = "proyecto", cascade = CascadeType.ALL, orphanRemoval = true)
    private PlanDeTrabajo planDeTrabajo;

    @OneToOne(mappedBy = "proyecto")
    private Estudiante estudiante;

    @ManyToOne
    @JoinColumn(name = "tutor_unrn_id")
    private Usuario tutorUNRN;

    @ManyToOne
    @JoinColumn(name = "tutor_externo_id")
    private TutorExterno tutorExterno;

    @ElementCollection
    private List<String> objetivos = new ArrayList<>();

    @Column(name = "progreso")
    private int progreso;

    @Enumerated(EnumType.STRING)
    private EstadoProyecto estado;

    public Proyecto(String titulo, String descripcion, Estudiante estudiante,
                    Usuario tutorUNRN, TutorExterno tutorExterno, Entidad entidad) {
        this.descripcion = descripcion;
        this.estudiante = estudiante;
        if (estudiante != null) {
            estudiante.setProyecto(this);
        }
        this.tutorUNRN = tutorUNRN;
        this.tutorExterno = tutorExterno;
        this.entidad = entidad;
        this.progreso = 0;
        this.proyectoId = new ProyectoId(titulo, entidad.getCuit());
        this.estado = EstadoProyecto.EN_ESPERA;
    }

    public void addObjetivo(String objetivo) {
        this.objetivos.add(objetivo);
    }

    public ProyectoId proyectoId() {
        return proyectoId;
    }

    public Entidad entidad() {
        return entidad;
    }

    public String getTitulo() {
        return proyectoId != null ? proyectoId.titulo() : null;
    }

    public void asignarEstudiante(Estudiante e) {
        this.estudiante = e;
        e.setProyecto(this);
    }

    public void setPlanDeTrabajo(PlanDeTrabajo planDeTrabajo) {
        this.planDeTrabajo = planDeTrabajo;
        if (planDeTrabajo != null && planDeTrabajo.getProyecto() != this) {
            planDeTrabajo.setProyecto(this);
        }
    }

    public enum EstadoProyecto {
        EN_ESPERA,
        EN_CURSO,
        FINALIZADO
    }
}