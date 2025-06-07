package ing.gpps.repository;

import ing.gpps.entity.institucional.Proyecto;
import ing.gpps.entity.idClasses.ProyectoId;
import ing.gpps.entity.users.DocenteSupervisor;
import ing.gpps.entity.users.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProyectoRepository extends JpaRepository<Proyecto, ProyectoId> {
    List<Proyecto> findByTutorUNRN(DocenteSupervisor tutor);
    Proyecto findByProyectoId_TituloAndProyectoId_CuitEntidad(String titulo, Long cuitEntidad);
    
    @Query("SELECT p FROM Proyecto p WHERE p.estudiante.id = :estudianteId")
    List<Proyecto> findByEstudiante(@Param("estudianteId") Long estudianteId);
    
    boolean existsByProyectoId_Titulo(String titulo);
    Optional<Proyecto> findByProyectoId_Titulo(String titulo);
}
