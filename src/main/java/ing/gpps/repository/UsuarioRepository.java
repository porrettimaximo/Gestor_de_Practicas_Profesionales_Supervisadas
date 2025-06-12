package ing.gpps.repository;

import ing.gpps.entity.users.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
    boolean existsByEmail(String email);

    //      Ejemplo de querys en JPA
//      @Query("SELECT u FROM Usuario u WHERE u.username = :username")
//      Usuario findByUsername(@Param("username") String username);
//
//      @Query("SELECT u FROM Usuario u")
//      List<Usuario> findAllUsers();
//
//      @Query("SELECT u FROM Usuario u WHERE u.nombre LIKE :nombre%")
//      List<Usuario> findByNombreStartingWith(@Param("nombre") String nombre);
//
//      Ejemplo de consulta derivada
//      List<Usuario> findByNombreYApellido(String nombre, String apellido);
}
