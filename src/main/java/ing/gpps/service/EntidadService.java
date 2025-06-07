package ing.gpps.service;

import ing.gpps.entity.institucional.Entidad;
import ing.gpps.entity.users.TutorExterno;
import ing.gpps.repository.EntidadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EntidadService {

    private final EntidadRepository entidadRepository;

    @Autowired
    public EntidadService(EntidadRepository entidadRepository) {
        this.entidadRepository = entidadRepository;
    }

    public void registrarEntidad(Entidad entidad) {
        entidadRepository.save(entidad);
    }

    public List<Entidad> obtenerTodas() {
        return entidadRepository.findAll();
    }

    public List<Entidad> getEntidadesByTutor(TutorExterno tutor) {
        return entidadRepository.findByTutorExterno(tutor);
    }
}
