package ing.gpps.service;

import ing.gpps.entity.institucional.Informe;
import ing.gpps.entity.institucional.Actividad;
import ing.gpps.entity.institucional.Proyecto;
import ing.gpps.entity.users.Estudiante;
import ing.gpps.repository.InformeRepository;
import ing.gpps.entity.idClasses.InformeId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class InformeService {

    private final InformeRepository informeRepository;
    private static final Logger logger = LoggerFactory.getLogger(InformeService.class);

    @Autowired
    public InformeService(InformeRepository informeRepository) {
        this.informeRepository = informeRepository;
    }

    public List<Informe> obtenerInformesPorProyecto(Proyecto proyecto) {
        if (proyecto == null) {
            throw new IllegalArgumentException("El proyecto no puede ser nulo");
        }
        return informeRepository.findByActividad_PlanDeTrabajo_Proyecto(proyecto);
    }

    public Informe evaluarInforme(Informe informe) {
        if (informe == null) {
            throw new IllegalArgumentException("El informe no puede ser nulo");
        }
        return informeRepository.save(informe);
    }

    @Transactional
    public Informe crearInforme(int numero, String titulo, String ruta, Estudiante estudiante, Actividad actividad) {
        if (estudiante == null || estudiante.getDni() == null) {
            throw new IllegalArgumentException("El estudiante y su DNI no pueden ser nulos");
        }
        if (actividad == null) {
            throw new IllegalArgumentException("La actividad no puede ser nula");
        }
        try {
            Informe informe = new Informe(numero, LocalDate.now(), titulo, ruta, estudiante, actividad);
            return informeRepository.save(informe);
        } catch (Exception e) {
            logger.error("Error al crear informe: {}", e.getMessage());
            throw new RuntimeException("Error al crear el informe", e);
        }
    }

    public Optional<Informe> obtenerInforme(InformeId id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID del informe no puede ser nulo");
        }
        return informeRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Informe> obtenerInformesPorEstudiante(Integer dni) {
        return informeRepository.findByEstudianteDni(dni);
    }

    public void eliminarInforme(InformeId id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID del informe no puede ser nulo");
        }
        informeRepository.deleteById(id);
    }

    public boolean existeInforme(InformeId id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID del informe no puede ser nulo");
        }
        return informeRepository.existsById(id);
    }

    public List<Informe> obtenerTodosLosInformes() {
        return informeRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Informe> obtenerInformesPorActividad(Actividad actividad) {
        return informeRepository.findByActividad(actividad);
    }

    public Informe guardarInforme(Informe informe) {
        return informeRepository.save(informe);
    }
}
