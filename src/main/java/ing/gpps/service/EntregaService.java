package ing.gpps.service;

import ing.gpps.entity.institucional.Entrega;
import ing.gpps.entity.institucional.Proyecto;
import ing.gpps.entity.users.Estudiante;
import ing.gpps.repository.EntregaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class EntregaService {

    @Autowired
    private EntregaRepository entregaRepository;

    @Transactional
    public Entrega crearEntrega(Entrega entrega) {
        entrega.setEstado(Entrega.EstadoEntrega.PENDIENTE);
        entrega.setFecha(LocalDate.now());
        return entregaRepository.save(entrega);
    }

    @Transactional
    public Entrega evaluarEntrega(Entrega entrega) {
        return entregaRepository.save(entrega);
    }

    @Transactional
    public Entrega guardar(Entrega entrega) {
        return entregaRepository.save(entrega);
    }

    @Transactional(readOnly = true)
    public List<Entrega> buscarPorProyecto(Proyecto proyecto) {
        return entregaRepository.findByActividad_PlanDeTrabajo_Proyecto(proyecto);
    }

    public List<Entrega> buscarEntregadasPorProyecto(Proyecto proyecto) {
        return entregaRepository.findByActividadAndEstadoOrderByFechaEntregaDesc(
                proyecto.getPlanDeTrabajo().getActividades().getFirst(), Entrega.EstadoEntrega.ENTREGADO);
    }

    public List<Entrega> buscarAprobadasPorProyecto(Proyecto proyecto) {
        return entregaRepository.findByActividadAndEstadoOrderByFechaEntregaDesc(
                proyecto.getPlanDeTrabajo().getActividades().getFirst(), Entrega.EstadoEntrega.APROBADO);
    }

    @Transactional
    public void actualizarEstado(Entrega entrega, Entrega.EstadoEntrega nuevoEstado) {
        entrega.setEstado(nuevoEstado);
        entregaRepository.save(entrega);
    }

    public Optional<Entrega> buscarPorId(long id) {
        return entregaRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Entrega findById(Long id) {
        return entregaRepository.findById(id).orElse(null);
    }
}
