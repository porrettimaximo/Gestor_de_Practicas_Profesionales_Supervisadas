package ing.gpps.notificaciones;

import ing.gpps.entity.users.Admin;
import ing.gpps.entity.users.Usuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Notificacion {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
    // Relaciones: muchos a uno (una notificación tiene un emisor y un destinatario)
    @ManyToOne
    @JoinColumn(name = "emisor_id")
    private Usuario emisor;

    private String mensaje;
    private String tipo;

    @ManyToOne
    @JoinColumn(name = "destinatario_id")
    private Usuario destinatario;
}
