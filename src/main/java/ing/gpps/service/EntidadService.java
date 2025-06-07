package ing.gpps.service;

import ing.gpps.entity.institucional.Entidad;
import ing.gpps.repository.EntidadRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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

    @Transactional
    public void eliminarEntidad(Long cuit) {
        entidadRepository.deleteByCuit(cuit);
    }

    public Entidad obtenerPorCuit(Long cuit) {

        Optional<Entidad> entidad = entidadRepository.findByCuit(cuit);

        if (!entidad.isPresent()){
            throw new RuntimeException("No existe una entidad con cuit"+cuit);
        }
        return entidad.get();
    }

    public void actualizarEntidad(Entidad entidad) {
        entidadRepository.save(entidad);
    }
}
