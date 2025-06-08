package ing.gpps.repository;

import ing.gpps.entity.institucional.Entidad;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EntidadRepository extends JpaRepository<Entidad, String> {
    Optional<Entidad> findByCuit(Long cuit);

    List<Entidad> findByNombreContainingIgnoreCase(String nombre);

    List<Entidad> findByEmailContainingIgnoreCase(String email);

    List<Entidad> findByTelefonoContainingIgnoreCase(String telefono);

    List<Entidad> findByUbicacionContainingIgnoreCase(String direccion);

    Entidad save(Entidad entidad);
}
