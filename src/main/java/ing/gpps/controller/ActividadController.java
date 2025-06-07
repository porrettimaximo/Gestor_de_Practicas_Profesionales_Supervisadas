package ing.gpps.controller;

import ing.gpps.dto.ActividadRequest;
import ing.gpps.entity.institucional.Actividad;
import ing.gpps.entity.institucional.PlanDeTrabajo;
import ing.gpps.entity.institucional.Proyecto;
import ing.gpps.entity.idClasses.ActividadId;
import ing.gpps.entity.idClasses.PlanDeTrabajoId;
import ing.gpps.entity.idClasses.ProyectoId;
import ing.gpps.service.ActividadService;
import ing.gpps.service.PlanDeTrabajoService;
import ing.gpps.service.ProyectoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/actividades")
public class ActividadController {

    private static final Logger logger = LoggerFactory.getLogger(ActividadController.class);

    @Autowired
    private ActividadService actividadService;

    @Autowired
    private PlanDeTrabajoService planDeTrabajoService;

    @Autowired
    private ProyectoService proyectoService;

    @PostMapping
    public ResponseEntity<?> crearActividad(@RequestBody ActividadRequest request) {
        try {
            if (request == null) {
                return ResponseEntity.badRequest().body("La solicitud no puede ser nula");
            }

            if (request.getTituloProyecto() == null || request.getCuitEntidad() == null) {
                return ResponseEntity.badRequest().body("El título del proyecto y el CUIT son requeridos");
            }

            // Obtener el proyecto
            Proyecto proyecto = proyectoService.getProyectoByTituloAndCuit(
                request.getTituloProyecto(), 
                request.getCuitEntidad()
            );

            if (proyecto == null) {
                return ResponseEntity.badRequest().body("Proyecto no encontrado");
            }

            // Obtener o crear el plan de trabajo
            PlanDeTrabajo planDeTrabajo = proyecto.getPlanDeTrabajo();
            if (planDeTrabajo == null) {
                planDeTrabajo = new PlanDeTrabajo(1, LocalDate.now(), LocalDate.now().plusMonths(6), proyecto);
                proyecto.setPlanDeTrabajo(planDeTrabajo);
                planDeTrabajo = planDeTrabajoService.actualizarPlanDeTrabajo(planDeTrabajo);
            }

            // Crear la actividad
            Actividad actividad = new Actividad(
                planDeTrabajo.getActividades().size() + 1,
                request.getNombre(),
                request.getDescripcion(),
                planDeTrabajo
            );

            if (request.getHoras() > 0) {
                actividad.setHoras(request.getHoras());
            }

            Actividad actividadCreada = actividadService.crearActividad(actividad);
            return ResponseEntity.ok(actividadCreada);
        } catch (Exception e) {
            logger.error("Error al crear actividad: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Error al crear la actividad: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> obtenerTodasLasActividades() {
        try {
            List<Actividad> actividades = actividadService.obtenerTodasLasActividades();
            if (actividades.isEmpty()) {
                return ResponseEntity.ok("No hay actividades registradas");
            }
            return ResponseEntity.ok(actividades);
        } catch (Exception e) {
            logger.error("Error al obtener actividades: {}", e.getMessage());
            return ResponseEntity.internalServerError().body("Error al obtener las actividades: " + e.getMessage());
        }
    }

    @GetMapping("/{numero}/{planNumero}/{proyectoTitulo}/{proyectoCuit}")
    public ResponseEntity<?> obtenerActividadPorId(
            @PathVariable int numero,
            @PathVariable int planNumero,
            @PathVariable String proyectoTitulo,
            @PathVariable Long proyectoCuit) {
        try {
            if (proyectoTitulo == null || proyectoCuit == null) {
                return ResponseEntity.badRequest().body("El título y CUIT del proyecto son requeridos");
            }
            if (numero <= 0 || planNumero <= 0) {
                return ResponseEntity.badRequest().body("Los números de actividad y plan deben ser positivos");
            }

            ProyectoId proyectoId = new ProyectoId(proyectoTitulo, proyectoCuit);
            PlanDeTrabajoId planDeTrabajoId = new PlanDeTrabajoId(planNumero, proyectoId);
            ActividadId actividadId = new ActividadId(numero, planDeTrabajoId);

            return actividadService.obtenerActividadPorId(actividadId)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            logger.error("Error al obtener actividad por ID: {}", e.getMessage());
            return ResponseEntity.internalServerError().body("Error al obtener la actividad: " + e.getMessage());
        }
    }

    @PutMapping("/{numero}/{planNumero}/{proyectoTitulo}/{proyectoCuit}")
    public ResponseEntity<?> actualizarActividad(
            @PathVariable int numero,
            @PathVariable int planNumero,
            @PathVariable String proyectoTitulo,
            @PathVariable Long proyectoCuit,
            @RequestBody Actividad actividad) {
        try {
            if (proyectoTitulo == null || proyectoCuit == null) {
                return ResponseEntity.badRequest().body("El título y CUIT del proyecto son requeridos");
            }
            if (numero <= 0 || planNumero <= 0) {
                return ResponseEntity.badRequest().body("Los números de actividad y plan deben ser positivos");
            }

            ProyectoId proyectoId = new ProyectoId(proyectoTitulo, proyectoCuit);
            PlanDeTrabajoId planDeTrabajoId = new PlanDeTrabajoId(planNumero, proyectoId);
            ActividadId actividadId = new ActividadId(numero, planDeTrabajoId);

            if (!actividadService.obtenerActividadPorId(actividadId).isPresent()) {
                return ResponseEntity.notFound().build();
            }

            actividad.setActividadId(actividadId);
            Actividad actividadActualizada = actividadService.guardarActividad(actividad);
            return ResponseEntity.ok(actividadActualizada);
        } catch (Exception e) {
            logger.error("Error al actualizar actividad: {}", e.getMessage());
            return ResponseEntity.internalServerError().body("Error al actualizar la actividad: " + e.getMessage());
        }
    }

    @DeleteMapping("/{numero}/{planNumero}/{proyectoTitulo}/{proyectoCuit}")
    public ResponseEntity<?> eliminarActividad(
            @PathVariable int numero,
            @PathVariable int planNumero,
            @PathVariable String proyectoTitulo,
            @PathVariable Long proyectoCuit) {
        try {
            if (proyectoTitulo == null || proyectoCuit == null) {
                return ResponseEntity.badRequest().body("El título y CUIT del proyecto son requeridos");
            }
            if (numero <= 0 || planNumero <= 0) {
                return ResponseEntity.badRequest().body("Los números de actividad y plan deben ser positivos");
            }

            ProyectoId proyectoId = new ProyectoId(proyectoTitulo, proyectoCuit);
            PlanDeTrabajoId planDeTrabajoId = new PlanDeTrabajoId(planNumero, proyectoId);
            ActividadId actividadId = new ActividadId(numero, planDeTrabajoId);

            if (!actividadService.obtenerActividadPorId(actividadId).isPresent()) {
                return ResponseEntity.notFound().build();
            }

            actividadService.eliminarActividad(actividadId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("Error al eliminar actividad: {}", e.getMessage());
            return ResponseEntity.internalServerError().body("Error al eliminar la actividad: " + e.getMessage());
        }
    }

    @PutMapping("/{numero}/{planNumero}/{proyectoTitulo}/{proyectoCuit}/estado")
    public ResponseEntity<?> actualizarEstado(
            @PathVariable int numero,
            @PathVariable int planNumero,
            @PathVariable String proyectoTitulo,
            @PathVariable Long proyectoCuit,
            @RequestParam Actividad.EstadoActividad nuevoEstado) {
        try {
            if (proyectoTitulo == null || proyectoCuit == null || nuevoEstado == null) {
                return ResponseEntity.badRequest().body("Todos los parámetros son requeridos");
            }
            if (numero <= 0 || planNumero <= 0) {
                return ResponseEntity.badRequest().body("Los números de actividad y plan deben ser positivos");
            }

            ProyectoId proyectoId = new ProyectoId(proyectoTitulo, proyectoCuit);
            PlanDeTrabajoId planDeTrabajoId = new PlanDeTrabajoId(planNumero, proyectoId);
            ActividadId actividadId = new ActividadId(numero, planDeTrabajoId);

            Actividad actividadActualizada = actividadService.actualizarEstado(actividadId, nuevoEstado);
            return ResponseEntity.ok(actividadActualizada);
        } catch (RuntimeException e) {
            logger.error("Error al actualizar estado de actividad: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Error al actualizar el estado: " + e.getMessage());
        }
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<?> obtenerActividadesPorEstado(@PathVariable Actividad.EstadoActividad estado) {
        try {
            List<Actividad> actividades = actividadService.obtenerActividadesPorEstado(estado);
            return ResponseEntity.ok(actividades);
        } catch (Exception e) {
            logger.error("Error al obtener actividades por estado: {}", e.getMessage());
            return ResponseEntity.internalServerError().body("Error al obtener las actividades: " + e.getMessage());
        }
    }

    @GetMapping("/progreso/{tituloProyecto}")
    public ResponseEntity<?> calcularProgresoProyecto(@PathVariable String tituloProyecto) {
        try {
            double progreso = actividadService.calcularProgresoProyecto(tituloProyecto);
            return ResponseEntity.ok(progreso);
        } catch (Exception e) {
            logger.error("Error al calcular progreso del proyecto: {}", e.getMessage());
            return ResponseEntity.internalServerError().body("Error al calcular el progreso: " + e.getMessage());
        }
    }
}
