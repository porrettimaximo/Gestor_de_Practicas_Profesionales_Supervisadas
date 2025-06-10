package ing.gpps.main;



import ing.gpps.notificaciones.NotificacionesService;
import ing.gpps.notificaciones.EventoNotificacion;
import ing.gpps.notificaciones.TipoEvento;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class NotificarServiceTest {


    private NotificacionesService notificationService;
    private FakeSubject testSubject;
    private FakeObserver observer1;
    private FakeObserver observer2;

    @BeforeEach
    void setUp() {
        notificationService = new NotificacionesService();
        testSubject = new FakeSubject();
        observer1 = new FakeObserver("Observador 1 estudiantePonele");
        observer2 = new FakeObserver("Observador 2 Docente ponele");

        notificationService.registrarSubject("test", testSubject);
    }

    @Test
    void testAgregarYNotificarObservador() {
        notificationService.agregarObservador("test", observer1);
        notificationService.agregarObservador("test", observer2);

        EventoNotificacion evento = new EventoNotificacion(
                TipoEvento.PROYECTO_CREADO,
                "Se creó un nuevo proyecto",
                "Admin",
                null
        );

        notificationService.notificarEvento("test", evento);

        List<String> notifs1 = observer1.getMensajes();
        List<String> notifs2 = observer2.getMensajes();

        assertEquals(1, notifs1.size());
        assertTrue(notifs1.get(0).contains("Se creó un nuevo proyecto"));

        assertEquals(1, notifs2.size());
        assertTrue(notifs2.get(0).contains("Admin"));
    }

    @Test
    void testRemoverObservador() {
        notificationService.agregarObservador("test", observer1);
        notificationService.removerObservador("test", observer1);

        EventoNotificacion evento = new EventoNotificacion(
                TipoEvento.DOCUMENTO_SUBIDO,
                "Documento actualizado",
                "Sistema",
                null
        );

        notificationService.notificarEvento("test", evento);

        assertEquals(0, observer1.getMensajes().size());
    }

}