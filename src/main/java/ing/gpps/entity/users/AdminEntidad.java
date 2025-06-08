package ing.gpps.entity.users;

import ing.gpps.entity.institucional.Entidad;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
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
    @JoinColumn(name = "cuit_entidad", referencedColumnName = "cuit", nullable = false)
    private Entidad entidad;

    public Entidad getEntidad() {
        return this.entidad;
    }
}
