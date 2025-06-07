package ing.gpps.repository;

import ing.gpps.entity.institucional.Entidad;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EntidadRepository extends JpaRepository<Entidad, String> {

    void deleteByCuit(Long cuit);

    Optional<Entidad> findByCuit(Long cuit);
}
