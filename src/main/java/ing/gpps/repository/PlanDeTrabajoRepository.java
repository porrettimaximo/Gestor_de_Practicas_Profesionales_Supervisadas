package ing.gpps.repository;

import ing.gpps.entity.idClasses.PlanDeTrabajoId;
import ing.gpps.entity.institucional.PlanDeTrabajo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PlanDeTrabajoRepository extends JpaRepository<PlanDeTrabajo, PlanDeTrabajoId> {

    Optional<PlanDeTrabajo> findById(PlanDeTrabajoId id);

    // Usar una consulta JPQL explícita
    @Query("SELECT p FROM PlanDeTrabajo p JOIN p.proyecto pr WHERE pr.entidad.cuit = :cuit")
//    @Query("SELECT p FROM PlanDeTrabajo p WHERE p.proyecto.entidad.cuit = :cuit")
    List<PlanDeTrabajo> findByCuitEntidad(@Param("cuit") Long cuit);
}
