package ing.gpps.controller;

import ing.gpps.entity.institucional.PlanDeTrabajo;
import ing.gpps.entity.institucional.Proyecto;
import ing.gpps.entity.idClasses.PlanDeTrabajoId;
import ing.gpps.entity.idClasses.ProyectoId;
import ing.gpps.service.PlanDeTrabajoService;
import ing.gpps.service.ProyectoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/planes-trabajo")
public class PlanDeTrabajoController {

    private static final Logger logger = LoggerFactory.getLogger(PlanDeTrabajoController.class);

    @Autowired
    private PlanDeTrabajoService planDeTrabajoService;

    @Autowired
    private ProyectoService proyectoService;

    @PostMapping
    public ResponseEntity<?> crearPlanDeTrabajo(@RequestBody PlanDeTrabajo planDeTrabajo) {
        try {
            if (planDeTrabajo == null) {
                return ResponseEntity.badRequest().body("El plan de trabajo no puede ser nulo");
            }
            if (planDeTrabajo.getProyecto() == null) {
                return ResponseEntity.badRequest().body("El plan de trabajo debe estar asociado a un proyecto");
            }
            PlanDeTrabajo planCreado = planDeTrabajoService.actualizarPlanDeTrabajo(planDeTrabajo);
            return ResponseEntity.ok(planCreado);
        } catch (Exception e) {
            logger.error("Error al crear plan de trabajo: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Error al crear el plan de trabajo: " + e.getMessage());
        }
    }

    @GetMapping("/{numero}/{proyectoTitulo}/{proyectoCuit}")
    public ResponseEntity<?> obtenerPlanDeTrabajo(
            @PathVariable int numero,
            @PathVariable String proyectoTitulo,
            @PathVariable Long proyectoCuit) {
        try {
            ProyectoId proyectoId = new ProyectoId(proyectoTitulo, proyectoCuit);
            PlanDeTrabajoId planId = new PlanDeTrabajoId(numero, proyectoId);
            
            return planDeTrabajoService.obtenerPlanDeTrabajoPorId(planId)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            logger.error("Error al obtener plan de trabajo: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Error al obtener el plan de trabajo: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> obtenerTodosLosPlanesDeTrabajo() {
        try {
            List<PlanDeTrabajo> planes = planDeTrabajoService.obtenerTodosLosPlanesDeTrabajo();
            return ResponseEntity.ok(planes);
        } catch (Exception e) {
            logger.error("Error al obtener planes de trabajo: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Error al obtener los planes de trabajo: " + e.getMessage());
        }
    }

    @PutMapping("/{numero}/{proyectoTitulo}/{proyectoCuit}")
    public ResponseEntity<?> actualizarPlanDeTrabajo(
            @PathVariable int numero,
            @PathVariable String proyectoTitulo,
            @PathVariable Long proyectoCuit,
            @RequestBody PlanDeTrabajo planDeTrabajo) {
        try {
            ProyectoId proyectoId = new ProyectoId(proyectoTitulo, proyectoCuit);
            PlanDeTrabajoId planId = new PlanDeTrabajoId(numero, proyectoId);
            
            if (!planDeTrabajoService.obtenerPlanDeTrabajoPorId(planId).isPresent()) {
                return ResponseEntity.notFound().build();
            }
            
            PlanDeTrabajo planActualizado = planDeTrabajoService.actualizarPlanDeTrabajo(planDeTrabajo);
            return ResponseEntity.ok(planActualizado);
        } catch (Exception e) {
            logger.error("Error al actualizar plan de trabajo: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Error al actualizar el plan de trabajo: " + e.getMessage());
        }
    }

    @DeleteMapping("/{numero}/{proyectoTitulo}/{proyectoCuit}")
    public ResponseEntity<?> eliminarPlanDeTrabajo(
            @PathVariable int numero,
            @PathVariable String proyectoTitulo,
            @PathVariable Long proyectoCuit) {
        try {
            ProyectoId proyectoId = new ProyectoId(proyectoTitulo, proyectoCuit);
            PlanDeTrabajoId planId = new PlanDeTrabajoId(numero, proyectoId);
            
            if (!planDeTrabajoService.obtenerPlanDeTrabajoPorId(planId).isPresent()) {
                return ResponseEntity.notFound().build();
            }
            
            planDeTrabajoService.eliminarPlanDeTrabajo(planId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("Error al eliminar plan de trabajo: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Error al eliminar el plan de trabajo: " + e.getMessage());
        }
    }
} 