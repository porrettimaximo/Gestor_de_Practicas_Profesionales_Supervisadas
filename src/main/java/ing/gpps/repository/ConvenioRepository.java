package ing.gpps.repository;

import ing.gpps.entity.institucional.Convenio;
import ing.gpps.entity.institucional.Proyecto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConvenioRepository extends JpaRepository<Convenio, Long> {
    Optional<Convenio> findByProyecto(Proyecto proyecto);
}
