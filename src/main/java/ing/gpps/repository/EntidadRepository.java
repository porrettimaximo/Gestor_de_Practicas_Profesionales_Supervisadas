package ing.gpps.repository;

import ing.gpps.entity.institucional.Entidad;
import ing.gpps.entity.users.DocenteSupervisor;
import ing.gpps.entity.users.TutorExterno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EntidadRepository extends JpaRepository<Entidad, String> {
    @Query("SELECT DISTINCT e FROM Entidad e JOIN e.proyectos p WHERE p.tutorExterno = :tutor")
    List<Entidad> findByTutorExterno(@Param("tutor") TutorExterno tutor);

    Optional<Entidad> findByCuit(Long cuit);

    List<Entidad> findByNombreContainingIgnoreCase(String nombre);

    List<Entidad> findByEmailContainingIgnoreCase(String email);

    List<Entidad> findByTelefonoContainingIgnoreCase(String telefono);

    List<Entidad> findByUbicacionContainingIgnoreCase(String direccion);

    Entidad save(Entidad entidad);
    boolean existsByCuit(Long cuit);
    Optional<Entidad> findByCuit(Long cuit);
    void deleteByCuit(Long cuit);
}
