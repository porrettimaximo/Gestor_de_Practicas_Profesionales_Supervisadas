package ing.gpps.entity.pps;

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
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "fecha_inicio")
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin_estimada")
    private LocalDate fechaFinEstimada;

    @ManyToOne
    @JoinColumn(name = "estudiante_id")
    private Estudiante estudiante;

    @ManyToOne
    @JoinColumn(name = "tutor_unrn_id")
    private Usuario tutorUNRN;

    @ManyToOne
    @JoinColumn(name = "tutor_externo_id")
    private TutorExterno tutorExterno;

    @Column(nullable = false)
    private String entidad;

    @OneToMany(mappedBy = "proyecto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Entrega> entregas = new ArrayList<>();

    @ElementCollection
    private List<String> objetivos = new ArrayList<>();

    @Column(name = "progreso")
    private int progreso;

    public Proyecto(String titulo, String descripcion, LocalDate fechaInicio, LocalDate fechaFinEstimada,
                    Estudiante estudiante, Usuario tutorUNRN, TutorExterno tutorExterno, String entidad) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fechaInicio = fechaInicio;
        this.fechaFinEstimada = fechaFinEstimada;
        this.estudiante = estudiante;
        this.tutorUNRN = tutorUNRN;
        this.tutorExterno = tutorExterno;
        this.entidad = entidad;
        this.progreso = 0;
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
}
