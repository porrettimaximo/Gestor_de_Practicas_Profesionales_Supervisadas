package ing.gpps.controller;

import ing.gpps.entity.institucional.Informe;
import ing.gpps.entity.institucional.Actividad;
import ing.gpps.entity.users.Estudiante;
import ing.gpps.entity.users.Usuario;
import ing.gpps.entity.idClasses.ActividadId;
import ing.gpps.entity.idClasses.PlanDeTrabajoId;
import ing.gpps.entity.idClasses.ProyectoId;
import ing.gpps.entity.idClasses.InformeId;
import ing.gpps.repository.InformeRepository;
import ing.gpps.repository.ActividadRepository;
import ing.gpps.repository.UsuarioRepository;
import ing.gpps.service.InformeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/informes")
public class InformeController {

    private final InformeRepository informeRepository;
    private final UsuarioRepository usuarioRepository;
    private final ActividadRepository actividadRepository;
    private final InformeService informeService;
    private static final Logger logger = LoggerFactory.getLogger(InformeController.class);

    @Autowired
    public InformeController(InformeRepository informeRepository,
                           UsuarioRepository usuarioRepository,
                           ActividadRepository actividadRepository,
                           InformeService informeService) {
        this.informeRepository = informeRepository;
        this.usuarioRepository = usuarioRepository;
        this.actividadRepository = actividadRepository;
        this.informeService = informeService;
    }

    @GetMapping("/estudiante/{dni}")
    public ResponseEntity<?> getInformesByEstudiante(@PathVariable Long dni) {
        try {
            if (dni == null) {
                return ResponseEntity.badRequest().body("El DNI del estudiante no puede ser nulo");
            }
            List<Informe> informes = informeRepository.findByEstudianteDni(dni.intValue());
            return ResponseEntity.ok(informes);
        } catch (Exception e) {
            logger.error("Error al obtener informes del estudiante: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Error al obtener los informes: " + e.getMessage());
        }
    }

    @GetMapping("/actividad/{numero}/{planNumero}/{proyectoTitulo}/{proyectoCuit}")
    public ResponseEntity<?> getInformesByActividad(
            @PathVariable int numero,
            @PathVariable int planNumero,
            @PathVariable String proyectoTitulo,
            @PathVariable Long proyectoCuit) {
        try {
            if (proyectoTitulo == null || proyectoCuit == null) {
                return ResponseEntity.badRequest().body("El título y CUIT del proyecto son requeridos");
            }

            ProyectoId proyectoId = new ProyectoId(proyectoTitulo, proyectoCuit);
            PlanDeTrabajoId planDeTrabajoId = new PlanDeTrabajoId(planNumero, proyectoId);
            ActividadId actividadId = new ActividadId(numero, planDeTrabajoId);

            Actividad actividad = actividadRepository.findById(actividadId)
                .orElseThrow(() -> new RuntimeException("Actividad no encontrada"));

            List<Informe> informes = informeRepository.findByActividad(actividad);
            return ResponseEntity.ok(informes);
        } catch (Exception e) {
            logger.error("Error al obtener informes de la actividad: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Error al buscar los informes: " + e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> crearInforme(@RequestBody Informe informe, Authentication authentication) {
        try {
            if (informe == null) {
                return ResponseEntity.badRequest().body("El informe no puede ser nulo");
            }

            Usuario usuario = usuarioRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            if (!(usuario instanceof Estudiante)) {
                return ResponseEntity.badRequest().body("Solo los estudiantes pueden crear informes");
            }

            Estudiante estudiante = (Estudiante) usuario;
            informe.setEstudiante(estudiante);
            informe.setFecha(LocalDate.now());

            Informe informeGuardado = informeService.crearInforme(
                informe.getId().getNumero(),
                informe.getTitulo(),
                informe.getRuta(),
                estudiante,
                informe.getActividad()
            );

            return ResponseEntity.ok(informeGuardado);
        } catch (Exception e) {
            logger.error("Error al crear informe: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Error al crear el informe: " + e.getMessage());
        }
    }

    @PutMapping("/{numero}/{estudianteDni}")
    public ResponseEntity<?> actualizarInforme(
            @PathVariable int numero,
            @PathVariable Long estudianteDni,
            @RequestBody Informe informe,
            Authentication authentication) {
        try {
            Usuario usuario = usuarioRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            if (!(usuario instanceof Estudiante)) {
                return ResponseEntity.badRequest().body("Solo los estudiantes pueden actualizar informes");
            }

            Estudiante estudiante = (Estudiante) usuario;
            if (!estudiante.getDni().equals(estudianteDni)) {
                return ResponseEntity.badRequest().body("No tienes permiso para actualizar este informe");
            }

            InformeId informeId = new InformeId(numero, estudianteDni.intValue());
            if (!informeService.existeInforme(informeId)) {
                return ResponseEntity.notFound().build();
            }

            informe.setId(informeId);
            informe.setEstudiante(estudiante);
            Informe informeActualizado = informeService.evaluarInforme(informe);
            return ResponseEntity.ok(informeActualizado);
        } catch (Exception e) {
            logger.error("Error al actualizar informe: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Error al actualizar el informe: " + e.getMessage());
        }
    }

    @DeleteMapping("/{numero}/{estudianteDni}")
    public ResponseEntity<?> eliminarInforme(
            @PathVariable int numero,
            @PathVariable Long estudianteDni,
            Authentication authentication) {
        try {
            Usuario usuario = usuarioRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            if (!(usuario instanceof Estudiante)) {
                return ResponseEntity.badRequest().body("Solo los estudiantes pueden eliminar informes");
            }

            Estudiante estudiante = (Estudiante) usuario;
            if (!estudiante.getDni().equals(estudianteDni)) {
                return ResponseEntity.badRequest().body("No tienes permiso para eliminar este informe");
            }

            InformeId informeId = new InformeId(numero, estudianteDni.intValue());
            if (!informeService.existeInforme(informeId)) {
                return ResponseEntity.notFound().build();
            }

            informeService.eliminarInforme(informeId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("Error al eliminar informe: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Error al eliminar el informe: " + e.getMessage());
        }
    }

    @GetMapping("/proyecto/{titulo}/{cuit}")
    public ResponseEntity<?> getInformesByProyecto(
            @PathVariable String titulo,
            @PathVariable Long cuit) {
        try {
            if (titulo == null || cuit == null) {
                return ResponseEntity.badRequest().body("El título y CUIT del proyecto son requeridos");
            }

            ProyectoId proyectoId = new ProyectoId(titulo, cuit);
            List<Informe> informes = informeRepository.findByActividad_PlanDeTrabajo_Proyecto_ProyectoId(proyectoId);
            return ResponseEntity.ok(informes);
        } catch (Exception e) {
            logger.error("Error al obtener informes del proyecto: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Error al obtener los informes: " + e.getMessage());
        }
    }
}
