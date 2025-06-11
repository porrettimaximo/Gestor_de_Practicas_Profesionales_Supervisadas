package ing.gpps.repository;

import ing.gpps.entity.Solicitud;
import ing.gpps.entity.institucional.Proyecto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SolicitudRepository extends JpaRepository<Solicitud, Long> {

    @Query("SELECT COALESCE(COUNT(s), 0) FROM Solicitud s WHERE s.proyecto = :proyecto AND s.estado = 'PENDIENTE'")
    Long countPendientesByProyecto(@Param("proyecto") Proyecto proyecto);

    @Query("SELECT s FROM Solicitud s WHERE s.proyecto = :proyecto AND s.estado = 'PENDIENTE'")
    List<Solicitud> findPendientesByProyecto(@Param("proyecto") Proyecto proyecto);

    @Query("SELECT s FROM Solicitud s LEFT JOIN FETCH s.proyecto p LEFT JOIN FETCH p.entidad WHERE s.id = :id")
    Optional<Solicitud> findByIdWithProyectoAndEntidad(@Param("id") Long id);
}
