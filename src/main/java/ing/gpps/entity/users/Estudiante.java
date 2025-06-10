package ing.gpps.entity.users;

import ing.gpps.entity.institucional.Informe;
import ing.gpps.entity.institucional.Proyecto;
//importacion de notificaciones a chequear
import ing.gpps.notificaciones.NotificacionesService;
import ing.gpps.notificaciones.Notificar;
import ing.gpps.service.EmailService;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
@DiscriminatorValue("ESTUDIANTE")
public class Estudiante extends Usuario implements Notificar {

    @Column(name = "dni", unique = true)
    private Long dni;

    @Column(name = "legajo", unique = true)
    private Long legajo;

    @Column(name = "fk_id_supervisor")
    Long fk_matricula_supervisor;

    @Column(name = "fk_tutor_externo")
    Long fk_tutor_externo;

    @OneToOne
    @JoinColumns({
            @JoinColumn(name = "fk_titulo_proyecto", referencedColumnName = "titulo"),
            @JoinColumn(name = "fk_cuit_entidad", referencedColumnName = "cuit")
    })
    private Proyecto proyecto;

    @OneToMany(mappedBy = "estudiante", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Informe> informes;

    //servicios inyectados para notificaciones
    @Transient
    @Autowired
    private  NotificacionesService notificacionesService;

    @Transient
    @Autowired
    private EmailService emailService;


    public Estudiante(String nombre, String apellido, String email, String password, Long dni, Long legajo, Long numTelefono) {
        super(nombre, apellido, email, password, numTelefono);
        this.dni = dni;
        this.legajo = legajo;
    }

    public Estudiante(String nombre, String apellido, String email, String password, Long telefono) {
        super(nombre, apellido, email, password, telefono);
    }

    @Override
    public String getRol() {
        return "ESTUDIANTE";
    }

    public void asignarProyecto(Proyecto p) {
        if (this.proyecto != p) {
            this.proyecto = p;
            if (p != null) {
                p.setEstudiante(this);
            }
        }
    }

    public Long getDni() {
        return this.dni;
    }

    @Override
    public void notificarAutomatico (String mensaje) {
        // Procesar la notificación por email
        procesarNotificacion(mensaje);
    }
    private void procesarNotificacion (String mensaje){
        try {
            String destinatario = this.getEmail();
            String asunto = "Notificación para Estudiante " + this.getNombre();
            String cuerpo = mensaje;
            emailService.enviarCorreo(destinatario, asunto, cuerpo);
            System.out.println("Procesando notificación: " + mensaje);
        } catch (Exception e) {
            System.err.println("Error al enviar la notificación por correoal estudiante " + this.getNombre () + e.getMessage());
        }
    }

    public void setProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
    }

}
