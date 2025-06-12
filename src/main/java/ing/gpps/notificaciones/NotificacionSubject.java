package ing.gpps.notificaciones;

import java.util.List;

public interface NotificacionSubject {
    void agregarObservador(Notificar observador);
    void eliminarObservador(Notificar observador);
    void notificarObservadores(String mensaje);
    List<Notificar> obtenerObservadores();

}
