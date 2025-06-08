package ing.gpps.entity.users;

import ing.gpps.entity.institucional.Entidad;
import ing.gpps.entity.institucional.Proyecto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
@DiscriminatorValue("TUTOR_EXTERNO")
public class TutorExterno extends Usuario {
    @Column
    private Long cuit;

    public TutorExterno(String nombre, String apellido, String email, String password, Long numTelefono) {
        super(nombre, apellido, email, password, numTelefono);
    }

    @Override
    public String getRol() {
        return "TUTOR_EXTERNO";
    }

    @ManyToOne
    @JoinColumn(name = "cuit_entidad", referencedColumnName = "cuit")
    private Entidad entidad;

    @OneToMany(mappedBy = "tutorExterno")
    private List<Proyecto> proyectos;

    public Long getTelefono() {
        return super.getNumTelefono();
    }
}
