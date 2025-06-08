package ing.gpps.repository;

import ing.gpps.entity.institucional.Proyecto;
import ing.gpps.entity.users.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EstudianteRepository extends JpaRepository<Estudiante, Long> {

    Optional<Estudiante> findByProyecto(Proyecto proyecto);
}
