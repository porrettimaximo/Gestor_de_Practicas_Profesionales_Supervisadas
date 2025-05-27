package ing.gpps.service;

import ing.gpps.entity.institucional.Entidad;
import ing.gpps.repository.EntidadRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EntidadService {

    private EntidadRepository entidadRepository;

    public EntidadService(EntidadRepository entidadRepository) {
        this.entidadRepository = entidadRepository;
    }

    public void registrarEntidad(Entidad entidad) {
        entidadRepository.save(entidad);
    }

    public List<Entidad> obtenerTodas() {
        return entidadRepository.findAll();
    }
}
