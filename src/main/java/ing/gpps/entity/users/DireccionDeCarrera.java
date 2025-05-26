package ing.gpps.entity.users;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@DiscriminatorValue("DIRECCION_CARRERA")
public class DireccionDeCarrera extends Usuario implements Notificar {

    public DireccionDeCarrera(String nombre, String apellido, String email, String password, Long num_telefono) {
        super(nombre, apellido, email, password, num_telefono);
    }

    @Override
    public String getRol() {
        return "DIRECCION_CARRERA";
    }

    @Override
    public void notificar(String mensaje) {
        //TODO: Implementar la lógica de notificación para la Dirección de Carrera
    }
}
