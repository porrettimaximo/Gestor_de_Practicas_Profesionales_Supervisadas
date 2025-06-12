package ing.gpps.repository;

import ing.gpps.entity.institucional.Proyecto;
import ing.gpps.entity.users.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EstudianteRepository extends JpaRepository<Estudiante, Long> {
    Optional<Estudiante> findByDni(Long dni);

    @EntityGraph(attributePaths = "proyecto")
    Optional<Estudiante> findByEmail(String email);
    boolean existsByDni(Long dni);

    Optional<Estudiante> findByProyecto(Proyecto proyecto);
}
