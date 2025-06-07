package ing.gpps.repository;

import ing.gpps.entity.institucional.PlanDeTrabajo;
import ing.gpps.entity.institucional.Proyecto;
import ing.gpps.entity.idClasses.PlanDeTrabajoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlanDeTrabajoRepository extends JpaRepository<PlanDeTrabajo, PlanDeTrabajoId> {
    Optional<PlanDeTrabajo> findByProyecto(Proyecto proyecto);
    boolean existsByPlanDeTrabajoId(PlanDeTrabajoId planDeTrabajoId);
}
