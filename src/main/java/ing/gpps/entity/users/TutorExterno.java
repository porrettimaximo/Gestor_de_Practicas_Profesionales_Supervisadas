package ing.gpps.entity.users;
//imports de notificaciones
import ing.gpps.notificaciones.NotificacionesService;
import ing.gpps.notificaciones.Notificar;
import ing.gpps.service.EmailService;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

@Entity
@NoArgsConstructor
@Getter
@Setter
@DiscriminatorValue("TUTOR_EXTERNO")
public class TutorExterno extends Usuario implements Notificar {
    //servicios inyectados para notificaciones
    @Transient
    @Autowired
    private NotificacionesService notificacionesService;

    @Transient
    @Autowired
    private EmailService emailService;


    public TutorExterno(String nombre, String apellido, String email, String password, Long numTelefono) {
        super(nombre, apellido, email, password, numTelefono);
    }

    @Override
    public String getRol() {
        return "TUTOR_EXTERNO";
    }

    @Override
    public void notificarAutomatico (String mensaje) {
        // Procesar la notificación por email
        procesarNotificacion(mensaje);
    }
    private void procesarNotificacion (String mensaje) {
        try {
            String destinatario = this.getEmail ();
            String asunto = "Notificación para el Tutor " + this.getNombre ();
            String cuerpo = mensaje;
            emailService.enviarCorreo (destinatario, asunto, cuerpo);
            System.out.println ("Procesando notificación: " + mensaje);
        } catch (Exception e) {
            System.err.println ("Error al enviar la notificación por correoal estudiante " + this.getNombre () + e.getMessage ());
        }
    }

}
