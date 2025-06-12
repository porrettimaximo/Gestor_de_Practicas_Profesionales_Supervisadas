package ing.gpps.notificaciones;

import java.time.LocalDateTime;

public class EventoNotificacion {
    private final TipoEvento tipo;
    private final String mensaje;
    private final String emisor;
    private final LocalDateTime timestamp;
    private final Object datos;

    public EventoNotificacion(TipoEvento tipo, String mensaje, String emisor, Object datos) {
        this.tipo = tipo;
        this.mensaje = mensaje;
        this.emisor = emisor;
        this.datos = datos;
        this.timestamp = LocalDateTime.now();
    }

    // Getters

    
    public TipoEvento getTipo() { return tipo; }
    public String getMensaje() { return mensaje; }
    public String getEmisor() { return emisor; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public Object getDatos() { return datos; }

    public String formatearMensaje() {
        return String.format("[%s] %s - %s (por %s)",
                timestamp.toString(), tipo.getDescripcion(), mensaje, emisor);
    }
}
