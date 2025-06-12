package ing.gpps.repository;

import ing.gpps.entity.institucional.Informe;
import ing.gpps.entity.institucional.Proyecto;
import ing.gpps.entity.idClasses.InformeId;
import ing.gpps.entity.institucional.Actividad;
import ing.gpps.entity.idClasses.ProyectoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InformeRepository extends JpaRepository<Informe, InformeId> {
    List<Informe> findByEstudianteDni(Integer dni);

    @Query("SELECT i FROM Informe i WHERE i.actividad = :actividad")
    List<Informe> findByActividad(@Param("actividad") Actividad actividad);

    @Query("SELECT i FROM Informe i WHERE i.actividad.planDeTrabajo.proyecto = :proyecto")
    List<Informe> findByActividad_PlanDeTrabajo_Proyecto(@Param("proyecto") Proyecto proyecto);

    @Query("SELECT i FROM Informe i WHERE i.actividad.planDeTrabajo.proyecto.proyectoId = :proyectoId")
    List<Informe> findByActividad_PlanDeTrabajo_Proyecto_ProyectoId(@Param("proyectoId") ProyectoId proyectoId);
}
