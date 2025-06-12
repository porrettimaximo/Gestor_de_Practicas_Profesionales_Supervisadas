package ing.gpps.repository;

import ing.gpps.entity.idClasses.ActividadId;
import ing.gpps.entity.institucional.Actividad;
import ing.gpps.entity.institucional.PlanDeTrabajo;
import ing.gpps.entity.idClasses.PlanDeTrabajoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActividadRepository extends JpaRepository<Actividad, ActividadId> {
    List<Actividad> findByPlanDeTrabajo_Proyecto_ProyectoId_Titulo(String tituloProyecto);
    List<Actividad> findByEstado(Actividad.EstadoActividad estado);
    List<Actividad> findByPlanDeTrabajo(PlanDeTrabajo planDeTrabajo);
    List<Actividad> findByPlanDeTrabajo_PlanDeTrabajoId(PlanDeTrabajoId planDeTrabajoId);
}
