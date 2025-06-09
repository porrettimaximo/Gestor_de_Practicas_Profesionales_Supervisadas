package ing.gpps.service;

import ing.gpps.entity.institucional.Entidad;
import ing.gpps.entity.users.AdminEntidad;
import ing.gpps.repository.AdminEntidadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminEntidadService {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private EntidadService entidadService;
    @Autowired
    private AdminEntidadRepository adminEntidadRepository;

    public void registrarAdminEntidad(String nombre, String apellido, String email, Long numTelefono, String password, Long cuit) {
        password = passwordEncoder.encode(password);
        AdminEntidad adminEntidad = new AdminEntidad(nombre, apellido, email, password, numTelefono);
        Entidad entidad = entidadService.obtenerPorCuit(cuit);
        adminEntidad.setEntidad(entidad);
        adminEntidadRepository.save(adminEntidad);
    }
}
