package ing.gpps.entity.users;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
@DiscriminatorValue("DOCENTE_SUPERVISOR")
public class DocenteSupervisor extends Usuario {

    public DocenteSupervisor(String nombre, String apellido, String email, String password, Long numTelefono) {
        super(nombre, apellido, email, password, numTelefono);
    }

    @Override
    public String getRol() {
        return "DOCENTE_SUPERVISOR";
    }
}
