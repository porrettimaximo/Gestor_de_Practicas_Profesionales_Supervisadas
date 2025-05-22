package ing.gpps.entity.users;

import ing.gpps.entity.institucional.Entidad;
import jakarta.persistence.*;

@Entity
@DiscriminatorValue("ADMIN_ENTIDAD")
public class AdminEntidad extends Usuario {
    public AdminEntidad(String nombre, String apellido, String email, String password, Long numTelefono) {
        super(nombre, apellido, email, password, numTelefono);
    }

    @Override
    public String getRol() {
        return "ADMIN_ENTIDAD";
    }

    @ManyToOne
    @JoinColumn(name = "cuit_entidad")
    private Entidad entidad;

}
