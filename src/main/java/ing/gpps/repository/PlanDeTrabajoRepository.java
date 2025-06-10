package ing.gpps.repository;

import ing.gpps.entity.idClasses.PlanDeTrabajoId;
import ing.gpps.entity.institucional.PlanDeTrabajo;
import ing.gpps.entity.institucional.Proyecto;
import ing.gpps.entity.idClasses.PlanDeTrabajoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlanDeTrabajoRepository extends JpaRepository<PlanDeTrabajo, PlanDeTrabajoId> {
    Optional<PlanDeTrabajo> findByProyecto(Proyecto proyecto);
    boolean existsByPlanDeTrabajoId(PlanDeTrabajoId planDeTrabajoId);
    Optional<PlanDeTrabajo> findById(PlanDeTrabajoId id);

    // Usar una consulta JPQL explícita
    @Query("SELECT p FROM PlanDeTrabajo p JOIN p.proyecto pr WHERE pr.entidad.cuit = :cuit")
//    @Query("SELECT p FROM PlanDeTrabajo p WHERE p.proyecto.entidad.cuit = :cuit")
    List<PlanDeTrabajo> findByCuitEntidad(@Param("cuit") Long cuit);
}
