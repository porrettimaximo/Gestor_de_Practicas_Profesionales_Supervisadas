package ing.gpps.service;

import ing.gpps.entity.institucional.Entrega;
import ing.gpps.entity.institucional.Proyecto;
import ing.gpps.repository.EntregaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class EntregaService {

    private final EntregaRepository entregaRepository;

    @Autowired
    public EntregaService(EntregaRepository entregaRepository) {
        this.entregaRepository = entregaRepository;
    }

    public Entrega guardar(Entrega entrega) {
        return entregaRepository.save(entrega);
    }

    public List<Entrega> buscarPorProyecto(Proyecto proyecto) {
        return entregaRepository.findByActividadOrderByFechaLimiteAsc(proyecto.getPlanDeTrabajo().getActividades().getFirst());
    }

    public List<Entrega> buscarEntregadasPorProyecto(Proyecto proyecto) {
        return entregaRepository.findByActividadAndEstadoOrderByFechaEntregaDesc(
                proyecto.getPlanDeTrabajo().getActividades().getFirst(), Entrega.EstadoEntrega.ENTREGADO);

    }

    public List<Entrega> buscarAprobadasPorProyecto(Proyecto proyecto) {
        return entregaRepository.findByActividadAndEstadoOrderByFechaEntregaDesc(
                proyecto.getPlanDeTrabajo().getActividades().getFirst(), Entrega.EstadoEntrega.APROBADO);
    }

    public void registrarEntrega(Entrega entrega, String archivoUrl, String tamanoArchivo, String comentarios) {
        entrega.setFechaEntrega(LocalDate.now());
        entrega.setEstado(Entrega.EstadoEntrega.ENTREGADO);
        entrega.setArchivoUrl(archivoUrl);
        entrega.setTamanoArchivo(tamanoArchivo);
        entrega.setComentarios(comentarios);
        entregaRepository.save(entrega);
    }

    public void cambiarEstado(Entrega entrega, Entrega.EstadoEntrega estado) {
        entrega.setEstado(estado);
        entregaRepository.save(entrega);
    }

    public Optional<Entrega> buscarPorId(int id) {
        return entregaRepository.findById(id);
    }
}
