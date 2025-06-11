package ing.gpps.service;

import ing.gpps.entity.Solicitud;
import ing.gpps.entity.institucional.Proyecto;
import ing.gpps.entity.users.Estudiante;
import ing.gpps.repository.SolicitudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SolicitudService {

    private final SolicitudRepository solicitudRepository;

    @Autowired
    public SolicitudService(SolicitudRepository solicitudRepository) {
        this.solicitudRepository = solicitudRepository;
    }

    @Transactional(readOnly = true)
    public List<Solicitud> getSolicitudesPendientesByProyecto(Proyecto proyecto) {
        return solicitudRepository.findPendientesByProyecto(proyecto);
    }

    @Transactional
    public Solicitud aprobarSolicitud(Long id) {
        Solicitud solicitud = solicitudRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));
        solicitud.aprobar();
        solicitud.getProyecto().asignarEstudiante((Estudiante) solicitud.getSolicitante());
        return solicitudRepository.save(solicitud);
    }

    @Transactional
    public Solicitud rechazarSolicitud(Long id) {
        Solicitud solicitud = solicitudRepository.findByIdWithProyectoAndEntidad(id)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));
        solicitud.rechazar();
        return solicitudRepository.save(solicitud);
    }
} 