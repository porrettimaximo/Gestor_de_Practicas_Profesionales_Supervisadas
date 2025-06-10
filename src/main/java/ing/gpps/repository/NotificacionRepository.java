package ing.gpps.repository;

import ing.gpps.entity.institucional.Entrega;
import ing.gpps.notificaciones.Notificacion;
import ing.gpps.notificaciones.Notificar;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {
}
