package ing.gpps.notificaciones;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class NotificacionesService {
    private final Map<String, NotificacionSubject> Asuntos = new ConcurrentHashMap<> ();

    public void registrarSubject(String identificador, NotificacionSubject subject) {
        Asuntos.put(identificador, subject);
    }

    public void agregarObservador(String subjectId, Notificar observer) {
        NotificacionSubject subject = Asuntos.get(subjectId);
        if (subject != null) {
            subject.agregarObservador (observer);
        }
    }

    public void removerObservador(String subjectId, Notificar observer) {
        NotificacionSubject subject = Asuntos.get(subjectId);
        if (subject != null) {
            subject.eliminarObservador (observer);
        }
    }

    public void notificarEvento(String subjectId, EventoNotificacion evento) {
        NotificacionSubject subject = Asuntos.get(subjectId);
        if (subject != null) {
            subject.notificarObservadores (evento.formatearMensaje());
        }
    }

    public void notificarTodos(EventoNotificacion evento) {
        Asuntos.values().forEach(subject ->
                subject.notificarObservadores (evento.formatearMensaje()));
    }

}