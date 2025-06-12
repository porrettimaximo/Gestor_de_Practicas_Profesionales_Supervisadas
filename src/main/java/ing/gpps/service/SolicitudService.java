package ing.gpps.service;

import ing.gpps.entity.Solicitud;
import ing.gpps.entity.institucional.Convenio;
import ing.gpps.entity.institucional.Proyecto;
import ing.gpps.entity.users.Estudiante;
import ing.gpps.repository.AdminEntidadRepository;
import ing.gpps.repository.ConvenioRepository;
import ing.gpps.repository.SolicitudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SolicitudService {

    private final SolicitudRepository solicitudRepository;
    private final ConvenioService convenioService;
    private final ConvenioRepository convenioRepository;

    @Autowired
    public SolicitudService(SolicitudRepository solicitudRepository, ConvenioService convenioService, ConvenioRepository convenioRepository) {
        this.solicitudRepository = solicitudRepository;
        this.convenioService = convenioService;
        this.convenioRepository = convenioRepository;
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
        solicitud.getProyecto().asignarEstudiante(solicitud.getSolicitante());

        Convenio convenio = new Convenio(
                solicitud.getSolicitante(),
                solicitud.getProyecto(),
                solicitud.getProyecto().getTutorExterno(),
                solicitud.getProyecto().getEntidad(),
                solicitud.getProyecto().getTutorUNRN()
        );
        convenioRepository.save(convenio);
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