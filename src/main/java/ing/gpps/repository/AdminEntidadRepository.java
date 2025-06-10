package ing.gpps.repository;

import ing.gpps.entity.users.AdminEntidad;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminEntidadRepository extends JpaRepository<AdminEntidad, Long> {

    AdminEntidad save(AdminEntidad adminEntidad);

    void findByEmail(String email);

    Optional<AdminEntidad> findByEntidadCuit(Long cuit);


}
