package ing.gpps.entity.users;

import ing.gpps.notificaciones.NotificacionesService;
import ing.gpps.notificaciones.Notificar;
import ing.gpps.service.EmailService;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Transient;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@DiscriminatorValue("DIRECCION_CARRERA")
public class DireccionDeCarrera extends Usuario implements Notificar {

    @Transient
    @Autowired
    private NotificacionesService notificacionesService;

    @Transient
    @Autowired
    private EmailService emailService;

    public DireccionDeCarrera(String nombre, String apellido, String email, String password, Long num_telefono) {
        super(nombre, apellido, email, password, num_telefono);
    }

    @Override
    public String getRol() {
        return "DIRECCION_CARRERA";
    }

    @Override
    public void notificarAutomatico (String mensaje) {
        //implementar EmailService
        procesarNotificacion(mensaje);

    }

    private void procesarNotificacion(String mensaje) {
        try {
            String destinatario = this.getEmail();
            String asunto = "Notificación para Direccion de Carrera " + this.getNombre();
            String cuerpo = mensaje;
            emailService.enviarCorreo(destinatario, asunto, cuerpo);
            System.out.println("Procesando notificación: " + mensaje);
        } catch (Exception e) {
            System.err.println("Error al enviar la notificación por correoal estudiante " + this.getNombre () + e.getMessage());
        }
    }

}
