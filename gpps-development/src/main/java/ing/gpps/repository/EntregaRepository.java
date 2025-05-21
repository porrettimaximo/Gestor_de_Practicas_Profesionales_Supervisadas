package ing.gpps.repository;


import ing.gpps.entity.pps.Entrega;
import ing.gpps.entity.pps.Proyecto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EntregaRepository extends JpaRepository<Entrega, Integer> {
    List<Entrega> findByProyecto(Proyecto proyecto);
    List<Entrega> findByProyectoOrderByFechaLimiteAsc(Proyecto proyecto);
    List<Entrega> findByProyectoAndEstadoOrderByFechaEntregaDesc(Proyecto proyecto, Entrega.EstadoEntrega estado);
}
