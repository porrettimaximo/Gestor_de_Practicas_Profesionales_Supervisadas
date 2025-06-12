package ing.gpps.entity.institucional;

import jakarta.persistence.*;
import ing.gpps.entity.users.Estudiante;
import ing.gpps.entity.users.TutorExterno;
import ing.gpps.entity.users.DocenteSupervisor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Convenio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "estudiante_id")
    private Estudiante estudiante;

    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "proyecto_titulo", referencedColumnName = "titulo"),
        @JoinColumn(name = "proyecto_cuit", referencedColumnName = "cuit")
    })
    private Proyecto proyecto;

    @OneToOne
    @JoinColumn(name = "tutor_externo_id")
    private TutorExterno tutorExterno;

    @OneToOne
    @JoinColumn(name = "cuit")
    private Entidad entidad;

    @OneToOne
    @JoinColumn(name = "docente_supervisor_id")
    private DocenteSupervisor docenteSupervisor;

    public Convenio(Estudiante estudiante, Proyecto proyecto, TutorExterno tutorExterno, Entidad entidad, DocenteSupervisor docenteSupervisor) {
        this.estudiante = estudiante;
        this.proyecto = proyecto;
        this.tutorExterno = tutorExterno;
        this.entidad = entidad;
        this.docenteSupervisor = docenteSupervisor;
    }


}
