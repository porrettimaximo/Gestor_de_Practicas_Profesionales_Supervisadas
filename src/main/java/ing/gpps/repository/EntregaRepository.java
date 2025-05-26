package ing.gpps.repository;


import ing.gpps.entity.institucional.Actividad;
import ing.gpps.entity.institucional.Entrega;
import ing.gpps.entity.institucional.PlanDeTrabajo;
import ing.gpps.entity.institucional.Proyecto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EntregaRepository extends JpaRepository<Entrega, Integer> {
    List<Entrega> findByActividad(Actividad actividad);
    List<Entrega> findByActividadOrderByFechaLimiteAsc(Actividad actividad);
    List<Entrega> findByActividadAndEstadoOrderByFechaEntregaDesc(Actividad actividad, Entrega.EstadoEntrega estado);
}
