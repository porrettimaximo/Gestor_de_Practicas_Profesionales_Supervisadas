package ing.gpps.service;

import ing.gpps.entity.institucional.Actividad;
import ing.gpps.entity.institucional.PlanDeTrabajo;
import ing.gpps.entity.idClasses.ActividadId;
import ing.gpps.entity.idClasses.PlanDeTrabajoId;
import ing.gpps.repository.ActividadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ActividadService {

    private final ActividadRepository actividadRepository;

    @Autowired
    public ActividadService(ActividadRepository actividadRepository) {
        this.actividadRepository = actividadRepository;
    }

    @Transactional
    public Actividad crearActividad(Actividad actividad) {
        actividad.setEstado(Actividad.EstadoActividad.EN_REVISION);
        return actividadRepository.save(actividad);
    }

    @Transactional(readOnly = true)
    public List<Actividad> obtenerTodasLasActividades() {
        return actividadRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Actividad> obtenerActividadPorId(ActividadId id) {
        return actividadRepository.findById(id);
    }

    @Transactional
    public Actividad guardarActividad(Actividad actividad) {
        return actividadRepository.save(actividad);
    }

    @Transactional
    public void eliminarActividad(ActividadId id) {
        actividadRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Actividad> obtenerActividadesPorPlanDeTrabajo(PlanDeTrabajo planDeTrabajo) {
        return actividadRepository.findByPlanDeTrabajo_PlanDeTrabajoId(planDeTrabajo.getPlanDeTrabajoId());
    }

    @Transactional(readOnly = true)
    public List<Actividad> obtenerActividadesPorEstado(Actividad.EstadoActividad estado) {
        return actividadRepository.findByEstado(estado);
    }

    @Transactional
    public Actividad actualizarEstado(ActividadId id, Actividad.EstadoActividad nuevoEstado) {
        Optional<Actividad> actividadOpt = actividadRepository.findById(id);
        if (actividadOpt.isPresent()) {
            Actividad actividad = actividadOpt.get();
            actividad.setEstado(nuevoEstado);
            return actividadRepository.save(actividad);
        }
        throw new RuntimeException("Actividad no encontrada con id: " + id);
    }

    @Transactional(readOnly = true)
    public double calcularProgresoProyecto(String tituloProyecto) {
        List<Actividad> actividades = actividadRepository.findByPlanDeTrabajo_Proyecto_ProyectoId_Titulo(tituloProyecto);
        int horasTotales = 200; // Horas totales del proyecto
        int horasCompletadas = actividades.stream()
                .filter(a -> a.getEstado() == Actividad.EstadoActividad.COMPLETADA)
                .mapToInt(Actividad::getHoras)
                .sum();
        
        return (double) horasCompletadas / horasTotales * 100;
    }

    public Actividad findById(Long id) {
        return actividadRepository.findAll().stream()
                .filter(a -> a.getActividadId().numero() == id)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Actividad no encontrada"));
    }
}
