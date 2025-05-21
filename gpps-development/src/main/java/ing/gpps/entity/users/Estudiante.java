package ing.gpps.entity.users;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
@DiscriminatorValue("ESTUDIANTE")
public class Estudiante extends Usuario {

    @Column(name = "dni", unique = true)
    private Long dni;

    @Column(name = "legajo", unique = true)
    private Long legajo;

    public Estudiante(String nombre, String apellido, String email, String password, Long dni, Long legajo, Long numTelefono) {
        super(nombre, apellido, email, password, numTelefono);
        this.dni = dni;
        this.legajo = legajo;
    }

    @Override
    public String getRol() {
        return "ESTUDIANTE";
    }
}
