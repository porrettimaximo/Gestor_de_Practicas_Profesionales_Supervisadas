package ing.gpps.service;

import ing.gpps.entity.institucional.Entrega;
import ing.gpps.entity.institucional.Proyecto;
import ing.gpps.entity.institucional.PlanDeTrabajo;
import ing.gpps.entity.institucional.Actividad;
import ing.gpps.entity.institucional.Actividad.EstadoActividad;
import ing.gpps.entity.idClasses.ActividadId;
import ing.gpps.entity.users.DocenteSupervisor;
import ing.gpps.repository.EntregaRepository;
import ing.gpps.repository.ProyectoRepository;
import ing.gpps.repository.ActividadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class DocenteSupervisorService {

    private final ProyectoRepository proyectoRepository;
    private final EntregaRepository entregaRepository;
    private final ActividadRepository actividadRepository;

    @Autowired
    public DocenteSupervisorService(ProyectoRepository proyectoRepository, 
                                  EntregaRepository entregaRepository,
                                  ActividadRepository actividadRepository) {
        this.proyectoRepository = proyectoRepository;
        this.entregaRepository = entregaRepository;
        this.actividadRepository = actividadRepository;
    }

    @Transactional(readOnly = true)
    public List<Proyecto> getProyectosByTutor(DocenteSupervisor tutor) {
        if (tutor == null) {
            return List.of();
        }
        return proyectoRepository.findByTutorUNRN(tutor);
    }

    @Transactional(readOnly = true)
    public Proyecto getProyectoByTituloAndCuit(String titulo, Long cuitEntidad) {
        if (titulo == null || cuitEntidad == null) {
            return null;
        }
        return proyectoRepository.findByProyectoId_TituloAndProyectoId_CuitEntidad(titulo, cuitEntidad);
    }

    @Transactional(readOnly = true)
    public List<Entrega> getEntregasByProyecto(Proyecto proyecto) {
        if (proyecto == null) {
            return List.of();
        }
        return entregaRepository.findByActividad_PlanDeTrabajo_Proyecto(proyecto);
    }

    @Transactional
    public void aprobarEntrega(Long id) {
        if (id == null) {
            return;
        }
        entregaRepository.findById(id).ifPresent(entrega -> {
            entrega.setEstado(Entrega.EstadoEntrega.APROBADO);
            entregaRepository.save(entrega);
        });
    }

    @Transactional
    public void rechazarEntrega(Long id) {
        if (id == null) {
            return;
        }
        entregaRepository.findById(id).ifPresent(entrega -> {
            entrega.setEstado(Entrega.EstadoEntrega.RECHAZADO);
            entregaRepository.save(entrega);
        });
    }

    @Transactional
    public void crearActividad(String cuitEntidad, String titulo, Actividad actividad) {
        if (cuitEntidad == null || titulo == null || actividad == null) {
            return;
        }
        Proyecto proyecto = getProyectoByTituloAndCuit(titulo, Long.parseLong(cuitEntidad));
        if (proyecto == null) {
            return;
        }
        
        PlanDeTrabajo planDeTrabajo = proyecto.getPlanDeTrabajo();
        if (planDeTrabajo == null) {
            // Crear nuevo plan de trabajo si no existe
            planDeTrabajo = new PlanDeTrabajo(1, LocalDate.now(), LocalDate.now().plusMonths(6), proyecto);
            proyecto.setPlanDeTrabajo(planDeTrabajo);
        }
        
        // Establecer el número de la actividad basado en el número actual de actividades
        int numeroActividad = planDeTrabajo.getActividades().size() + 1;
        ActividadId actividadId = new ActividadId(numeroActividad, planDeTrabajo.getPlanDeTrabajoId());
        actividad.setActividadId(actividadId);
        
        // Validar que la fecha límite no sea anterior a la fecha actual
        if (actividad.getFechaLimite() != null && actividad.getFechaLimite().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha límite no puede ser anterior a la fecha actual");
        }
        
        // Establecer el estado inicial de la actividad
        actividad.setEstado(EstadoActividad.EN_REVISION);
        
        planDeTrabajo.addActividad(actividad);
        proyectoRepository.save(proyecto);
    }

    @Transactional(readOnly = true)
    public Actividad getActividadById(int actividadId) {
        // Buscar la actividad en todos los planes de trabajo
        return actividadRepository.findAll().stream()
            .filter(a -> a.getActividadId().numero() == actividadId)
            .findFirst()
            .orElse(null);
    }

    @Transactional
    public Actividad guardarActividad(Actividad actividad) {
        return actividadRepository.save(actividad);
    }

    @Transactional(readOnly = true)
    public Entrega getEntregaById(Long entregaId) {
        return entregaRepository.findById(entregaId).orElse(null);
    }

    @Transactional
    public Entrega guardarEntrega(Entrega entrega) {
        return entregaRepository.save(entrega);
    }
}