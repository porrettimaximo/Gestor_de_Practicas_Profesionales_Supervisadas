package ing.gpps.service;

import ing.gpps.entity.institucional.Actividad;
import ing.gpps.entity.institucional.Informe;
import ing.gpps.entity.institucional.Proyecto;
import ing.gpps.entity.users.Estudiante;
import ing.gpps.repository.ActividadRepository;
import ing.gpps.repository.EstudianteRepository;
import ing.gpps.repository.ProyectoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class EstudianteService {
    private static final Logger logger = LoggerFactory.getLogger(EstudianteService.class);

    @Autowired
    private ActividadRepository actividadRepository;

    @Autowired
    private InformeService informeService;

    @Autowired
    private EstudianteRepository estudianteRepository;

    @Autowired
    private ProyectoRepository proyectoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public Actividad getActividadById(int actividadId) {
        return actividadRepository.findAll().stream()
            .filter(a -> a.getActividadId().numero() == actividadId)
            .findFirst()
            .orElse(null);
    }

    @Transactional
    public Informe guardarInforme(Informe informe) {
        return informeService.guardarInforme(informe);
    }

    @Transactional(readOnly = true)
    public List<Informe> obtenerInformesPorEstudiante(Integer dni) {
        return informeService.obtenerInformesPorEstudiante(dni);
    }

    @Transactional(readOnly = true)
    public Optional<Estudiante> buscarPorDni(Long dni) {
        return estudianteRepository.findByDni(dni);
    }

    @Transactional(readOnly = true)
    public Optional<Estudiante> buscarPorEmail(String email) {
        return estudianteRepository.findByEmail(email);
    }

    @Transactional(readOnly = true)
    public List<Proyecto> obtenerProyectosPorEstudiante(Estudiante estudiante) {
        if (estudiante == null) {
            logger.warn("Se intentó obtener proyectos para un estudiante nulo");
            return Collections.emptyList();
        }
        try {
            List<Proyecto> proyectos = proyectoRepository.findByEstudiante(estudiante.getId().longValue());
            return proyectos != null ? proyectos : Collections.emptyList();
        } catch (Exception e) {
            logger.error("Error al obtener proyectos para el estudiante {}: {}", 
                        estudiante.getEmail(), e.getMessage());
            return Collections.emptyList();
        }
    }

    @Transactional
    public Estudiante guardarEstudiante(Estudiante estudiante) {
        return estudianteRepository.save(estudiante);
    }

    @Transactional(readOnly = true)
    public boolean existeEstudiante(Long dni) {
        return estudianteRepository.existsByDni(dni);
    }

    @Transactional(readOnly = true)
    public List<Estudiante> obtenerTodosLosEstudiantes() {
        return estudianteRepository.findAll();
    }

    public void registrarUsuario(String nombre, String apellido, String email, Long numTelefono, String password, Long legajo, Long dni) {
        password = passwordEncoder.encode(password);
        Estudiante estudiante = new Estudiante(nombre, apellido, email, password, dni, legajo, numTelefono);
        estudianteRepository.save(estudiante);
    }
}