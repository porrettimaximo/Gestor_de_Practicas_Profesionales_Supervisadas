package ing.gpps.entity.institucional;

import jakarta.persistence.*;
import ing.gpps.entity.users.Estudiante;
import ing.gpps.entity.institucional.Proyecto;
import ing.gpps.entity.users.TutorExterno;
import ing.gpps.entity.users.DocenteSupervisor;
import ing.gpps.entity.users.DireccionDeCarrera;
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

    @ManyToOne
    @JoinColumn(name = "tutor_externo_id")
    private TutorExterno tutorExterno;

    @ManyToOne
    @JoinColumn(name = "entidad_id")
    private Entidad entidad;

    @ManyToOne
    @JoinColumn(name = "docente_supervisor_id")
    private DocenteSupervisor docenteSupervisor;

    @ManyToOne
    @JoinColumn(name = "direccion_carrera_id")
    private DireccionDeCarrera direccionCarrera;

}
