package ing.gpps.repository;

import ing.gpps.entity.users.TutorExterno;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TutorRepository extends JpaRepository<TutorExterno, Long> {

    Optional<TutorExterno> findByEmail(String email);

    List<TutorExterno> findAllByCuit(Long cuit);
}
