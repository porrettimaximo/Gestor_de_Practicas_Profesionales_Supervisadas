package ing.gpps.entity.users;

import ing.gpps.entity.institucional.Entidad;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
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
