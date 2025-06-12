package ing.gpps.notificaciones;

import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class BaseDeNotificacionSubject implements NotificacionSubject {
    private final List<Notificar> observadores = new CopyOnWriteArrayList<> ();

    @Override
    public void agregarObservador(Notificar observador) {
        if (observador != null && !observadores.contains(observador)) {
            observadores.add(observador);
        }
    }
    @Override
    public void eliminarObservador(Notificar observer) {
        observadores.remove(observer);
    }

    @Override
    public void notificarObservadores(String mensaje) {
        for (Notificar observer : observadores) {
            try {
                observer.notificarAutomatico(mensaje);
            } catch (Exception e) {
                // Log del error sin interrumpir otras notificaciones
                System.err.println("Error al notificar a " + observer.getClass().getSimpleName() + ": " + e.getMessage());
            }
        }
    }

    @Override
    public List<Notificar> obtenerObservadores() {
        return new ArrayList<>(observadores);
    }

}
