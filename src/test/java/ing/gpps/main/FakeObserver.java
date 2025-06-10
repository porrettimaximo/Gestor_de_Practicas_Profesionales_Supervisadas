package ing.gpps.main;

import ing.gpps.notificaciones.Notificar;
import lombok.Getter;

import java.util.List;

public class FakeObserver implements Notificar {

    private final String nombre;
    @Getter
    private final List<String> mensajes = new java.util.ArrayList<>();

    public FakeObserver(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public void notificarAutomatico(String mensaje) {
        mensajes.add("[" + nombre + "] " + mensaje);
    }

}
