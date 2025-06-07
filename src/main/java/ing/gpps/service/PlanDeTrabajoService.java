package ing.gpps.service;

import ing.gpps.entity.institucional.PlanDeTrabajo;
import ing.gpps.entity.institucional.Proyecto;
import ing.gpps.entity.idClasses.PlanDeTrabajoId;
import ing.gpps.repository.PlanDeTrabajoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PlanDeTrabajoService {

    private final PlanDeTrabajoRepository planDeTrabajoRepository;

    @Autowired
    public PlanDeTrabajoService(PlanDeTrabajoRepository planDeTrabajoRepository) {
        this.planDeTrabajoRepository = planDeTrabajoRepository;
    }

    public PlanDeTrabajo crearPlanDeTrabajo(int numero, LocalDate fechaInicio, LocalDate fechaFin, Proyecto proyecto) {
        PlanDeTrabajo planDeTrabajo = new PlanDeTrabajo(numero, fechaInicio, fechaFin, proyecto);
        return planDeTrabajoRepository.save(planDeTrabajo);
    }

    public Optional<PlanDeTrabajo> obtenerPlanDeTrabajoPorId(PlanDeTrabajoId id) {
        return planDeTrabajoRepository.findById(id);
    }

    public List<PlanDeTrabajo> obtenerTodosLosPlanesDeTrabajo() {
        return planDeTrabajoRepository.findAll();
    }

    public PlanDeTrabajo actualizarPlanDeTrabajo(PlanDeTrabajo planDeTrabajo) {
        return planDeTrabajoRepository.save(planDeTrabajo);
    }

    public void eliminarPlanDeTrabajo(PlanDeTrabajoId id) {
        planDeTrabajoRepository.deleteById(id);
    }

    public Optional<PlanDeTrabajo> obtenerPlanDeTrabajoPorProyecto(Proyecto proyecto) {
        return planDeTrabajoRepository.findByProyecto(proyecto);
    }
} 