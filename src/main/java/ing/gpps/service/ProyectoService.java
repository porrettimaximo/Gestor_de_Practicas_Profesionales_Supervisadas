package ing.gpps.service;


import ing.gpps.entity.users.Estudiante;
import ing.gpps.entity.pps.Proyecto;
import ing.gpps.repository.ProyectoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProyectoService {

    private final ProyectoRepository proyectoRepository;

    @Autowired
    public ProyectoService(ProyectoRepository proyectoRepository) {
        this.proyectoRepository = proyectoRepository;
    }

    public Proyecto guardar(Proyecto proyecto) {
        return proyectoRepository.save(proyecto);
    }

    public List<Proyecto> buscarPorEstudiante(Estudiante estudiante) {
        return proyectoRepository.findByEstudiante(estudiante);
    }

    public Optional<Proyecto> buscarPorEstudianteYId(Estudiante estudiante, int id) {
        return proyectoRepository.findByEstudianteAndId(estudiante, id);
    }

    public void actualizarProgreso(Proyecto proyecto, int progreso) {
        proyecto.setProgreso(progreso);
        proyectoRepository.save(proyecto);
    }
}
