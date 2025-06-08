package ing.gpps.repository;

import ing.gpps.entity.idClasses.ProyectoId;
import ing.gpps.entity.users.Estudiante;
import ing.gpps.entity.institucional.Proyecto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProyectoRepository extends JpaRepository<Proyecto, Integer> {
    List<Proyecto> findByEstudiante(Estudiante estudiante);

    Proyecto findByProyectoIdCuitEntidad(Long cuit);
    //Optional<Proyecto> findByEstudianteAndId(Estudiante estudiante, ProyectoId proyectoId);

    @Query("""
            SELECT p FROM Proyecto p
            WHERE p.entidad.cuit = :cuitEntidad
            AND p.planDeTrabajo IS NULL
            """)
    List<Proyecto> findWithoutPlanDeTrabajoByEntidad(@Param("cuitEntidad") Long cuitEntidad);

    Optional<Proyecto> findByProyectoId(ProyectoId id);

    List<Proyecto> findAllByProyectoIdCuitEntidad(Long cuitEntidad);
}
