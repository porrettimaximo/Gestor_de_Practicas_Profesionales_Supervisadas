package ing.gpps.service;

import ing.gpps.entity.users.Estudiante;
import ing.gpps.entity.users.DocenteSupervisor;
import ing.gpps.entity.institucional.Proyecto;
import ing.gpps.entity.idClasses.ProyectoId;
import ing.gpps.repository.ProyectoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
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
        if (estudiante == null) {
            return Collections.emptyList();
        }
        return proyectoRepository.findByEstudiante(estudiante.getId().longValue());
    }

    public List<Proyecto> buscarPorSupervisor(DocenteSupervisor supervisor) {
        return proyectoRepository.findByTutorUNRN(supervisor);
    }

    public Optional<Proyecto> buscarPorId(ProyectoId id) {
        return proyectoRepository.findById(id);
    }

    public void actualizarProgreso(Proyecto proyecto, int progreso) {
        proyecto.setProgreso(progreso);
        proyectoRepository.save(proyecto);
    }

    public Proyecto getProyectoByTituloAndCuit(String titulo, Long cuit) {
        // Log de entrada
        System.out.println("Buscando proyecto con titulo: " + titulo + ", cuit: " + cuit);

        Proyecto proyecto = proyectoRepository.findByProyectoId_TituloAndProyectoId_CuitEntidad(titulo, cuit);

        // Log de salida
        if (proyecto != null) {
            System.out.println("Proyecto encontrado: " + proyecto);
        } else {
            System.out.println("No se encontró ningún proyecto con titulo: " + titulo + ", cuit: " + cuit);
        }

        return proyecto;
    }

    public Proyecto findByEstudiante(Estudiante estudiante) {
        if (estudiante == null) {
            return null;
        }
        List<Proyecto> proyectos = proyectoRepository.findByEstudiante(estudiante.getId().longValue());
        return proyectos.isEmpty() ? null : proyectos.get(0);
    }
}
