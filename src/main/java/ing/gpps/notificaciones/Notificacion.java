package ing.gpps.notificaciones;

import ing.gpps.entity.users.Admin;
import ing.gpps.entity.users.Usuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "notificaciones")
public class Notificacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relaciones: muchos a uno (una notificación tiene un emisor y un destinatario)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emisor_id")
    private Usuario emisor;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destinatario_id")
    private Usuario destinatario;

    @Column(columnDefinition = "TEXT")
    private String mensaje;

    @Column(length = 50)
    private String tipo;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_lectura")
    private LocalDateTime fechaLectura;

    @Column(name = "leida")
    private Boolean leida = false;

    @Column(name = "importante")
    private Boolean importante = false;

    // Constructor personalizado para facilitar la creación
    public Notificacion(Usuario emisor, Usuario destinatario, String mensaje, String tipo) {
        this.emisor = emisor;
        this.destinatario = destinatario;
        this.mensaje = mensaje;
        this.tipo = tipo;
        this.fechaCreacion = LocalDateTime.now();
        this.leida = false;
        this.importante = false;
    }

    // Métodos de utilidad
    public String getFechaCreacionFormateada() {
        if (fechaCreacion != null) {
            return fechaCreacion.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        }
        return "";
    }

    public String getFechaLecturaFormateada() {
        if (fechaLectura != null) {
            return fechaLectura.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        }
        return "";
    }

    public String getTiempoTranscurrido() {
        if (fechaCreacion == null) return "";

        LocalDateTime ahora = LocalDateTime.now();
        long minutos = java.time.Duration.between(fechaCreacion, ahora).toMinutes();

        if (minutos < 1) {
            return "Hace un momento";
        } else if (minutos < 60) {
            return "Hace " + minutos + " minuto" + (minutos > 1 ? "s" : "");
        } else if (minutos < 1440) { // menos de 24 horas
            long horas = minutos / 60;
            return "Hace " + horas + " hora" + (horas > 1 ? "s" : "");
        } else {
            long dias = minutos / 1440;
            return "Hace " + dias + " día" + (dias > 1 ? "s" : "");
        }
    }

    public String getIconoTipo() {
        if (tipo == null) return "fas fa-info-circle";

        switch (tipo.toUpperCase()) {
            case "PROYECTO_CREADO":
                return "fas fa-plus-circle";
            case "PROYECTO_MODIFICADO":
                return "fas fa-edit";
            case "PROYECTO_COMPLETADO":
                return "fas fa-check-circle";
            case "DOCUMENTO_SUBIDO":
                return "fas fa-file-upload";
            case "EVALUACION_REALIZADA":
                return "fas fa-star";
            case "COMENTARIO_AGREGADO":
                return "fas fa-comment";
            case "FECHA_LIMITE_PROXIMA":
                return "fas fa-clock";
            default:
                return "fas fa-bell";
        }
    }

    public String getColorTipo() {
        if (tipo == null) return "#6c757d";

        switch (tipo.toUpperCase()) {
            case "PROYECTO_CREADO":
                return "#28a745"; // Verde
            case "PROYECTO_MODIFICADO":
                return "#ffc107"; // Amarillo
            case "PROYECTO_COMPLETADO":
                return "#28a745"; // Verde
            case "DOCUMENTO_SUBIDO":
                return "#17a2b8"; // Azul claro
            case "EVALUACION_REALIZADA":
                return "#fd7e14"; // Naranja
            case "COMENTARIO_AGREGADO":
                return "#6f42c1"; // Púrpura
            case "FECHA_LIMITE_PROXIMA":
                return "#dc3545"; // Rojo
            default:
                return "#6c757d"; // Gris
        }
    }

    public String getMensajeResumido(int maxLength) {
        if (mensaje == null) return "";
        if (mensaje.length() <= maxLength) return mensaje;
        return mensaje.substring(0, maxLength - 3) + "...";
    }

    // Métodos para compatibilidad con Lombok
    public Boolean isLeida() {
        return leida != null && leida;
    }

    public Boolean isImportante() {
        return importante != null && importante;
    }

    @PrePersist
    public void prePersist() {
        if (fechaCreacion == null) {
            fechaCreacion = LocalDateTime.now();
        }
        if (leida == null) {
            leida = false;
        }
        if (importante == null) {
            importante = false;
        }
    }
}
