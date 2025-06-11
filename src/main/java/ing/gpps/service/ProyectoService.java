package ing.gpps.service;

import ing.gpps.entity.Solicitud;
import ing.gpps.entity.institucional.Actividad;
import ing.gpps.entity.users.Estudiante;
import ing.gpps.entity.users.DocenteSupervisor;
import ing.gpps.entity.institucional.Proyecto;
import ing.gpps.entity.idClasses.ProyectoId;
import ing.gpps.repository.ProyectoRepository;
import ing.gpps.repository.SolicitudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ProyectoService {

    private final ProyectoRepository proyectoRepository;
    private final SolicitudRepository solicitudRepository;

    @Autowired
    public ProyectoService(ProyectoRepository proyectoRepository, 
                          SolicitudRepository solicitudRepository) {
        this.proyectoRepository = proyectoRepository;
        this.solicitudRepository = solicitudRepository;
    }

    public Proyecto guardar(Proyecto proyecto) {
        return proyectoRepository.save(proyecto);
    }

    public List<Proyecto> buscarPorEstudiante(Estudiante estudiante) {
        if (estudiante == null) {
            return Collections.emptyList();
        }
        return proyectoRepository.findByEstudiante(estudiante.getId().longValue());
    }

    public List<Proyecto> buscarPorSupervisor(DocenteSupervisor supervisor) {
        return proyectoRepository.findByTutorUNRN(supervisor);
    }

    public Optional<Proyecto> buscarPorId(ProyectoId id) {
        return proyectoRepository.findById(id);
    }

    public void actualizarProgreso(Proyecto proyecto, int progreso) {
        proyecto.setProgreso(progreso);
        proyectoRepository.save(proyecto);
    }

    public List<Proyecto> obtenerTodos() {
        return proyectoRepository.findAll();
    }

    public Proyecto getProyectoByTituloAndCuit(String titulo, Long cuit) {
        // Log de entrada
        System.out.println("Buscando proyecto con titulo: " + titulo + ", cuit: " + cuit);

        Proyecto proyecto = proyectoRepository.findByProyectoId_TituloAndProyectoId_CuitEntidad(titulo, cuit);

        // Log de salida
        if (proyecto != null) {
            System.out.println("Proyecto encontrado: " + proyecto);
        } else {
            System.out.println("No se encontró ningún proyecto con titulo: " + titulo + ", cuit: " + cuit);
        }

        return proyecto;
    }

    public Proyecto findByEstudiante(Estudiante estudiante) {
        if (estudiante == null) {
            return null;
        }
        List<Proyecto> proyectos = proyectoRepository.findByEstudiante(estudiante.getId().longValue());
        return proyectos.isEmpty() ? null : proyectos.get(0);
    }

    public List<Proyecto> obtenerProyectosActivos() {

        List<Proyecto> proyectos = proyectoRepository.findAll();

        return proyectos.stream()
                .filter(proyecto -> proyecto.getEstado() != null && proyecto.getEstado().isActivo())
                .toList();
    }

    public void finalizarPPS(String titulo, Long cuit) {
        Proyecto proyecto = proyectoRepository.findByProyectoId_TituloAndProyectoId_CuitEntidad(titulo, cuit);

        if(proyecto != null) {
            proyecto.setEstado(Proyecto.EstadoProyecto.FINALIZADO);
            proyectoRepository.save(proyecto);
            System.out.println("Proyecto finalizado: " + titulo + ", CUIT: " + cuit);
        } else {
            System.out.println("No se encontró el proyecto para finalizar: " + titulo + ", CUIT: " + cuit);
        }

    }

    public List<Proyecto> obtenerProyectosConPostulantes() {
        System.out.println("Obteniendo proyectos con postulantes...");
        List<Proyecto> proyectosEnEspera = proyectoRepository.findByEstado(Proyecto.EstadoProyecto.EN_ESPERA);
        System.out.println("Total de proyectos en espera: " + proyectosEnEspera.size());
        proyectosEnEspera.forEach(p -> System.out.println("Proyecto en espera: " + p.getTitulo()));
        return proyectosEnEspera;
    }

    public Map<Proyecto, Integer> obtenerCantidadPostulantesPorProyecto(List<Proyecto> proyectos) {
        Map<Proyecto, Integer> cantidadPostulantes = new HashMap<>();
        
        for (Proyecto proyecto : proyectos) {
            Long cantidad = solicitudRepository.countPendientesByProyecto(proyecto);
            cantidadPostulantes.put(proyecto, cantidad != null ? cantidad.intValue() : 0);
        }
        
        return cantidadPostulantes;
    }

    @Transactional(readOnly = true)
    public List<Proyecto> obtenerProyectosDisponibles(Estudiante estudiante) {
        List<Proyecto> proyectos = proyectoRepository.findByEstado(Proyecto.EstadoProyecto.EN_ESPERA);
        
        // Filtrar los proyectos donde el estudiante ha sido rechazado
        List<Proyecto> proyectosRechazados = proyectoRepository.findBySolicitudes_EstudianteAndSolicitudes_Estado(
            estudiante, 
            Solicitud.EstadoSolicitud.RECHAZADA
        );
        
        proyectos.removeAll(proyectosRechazados);
        
        System.out.println("Proyectos disponibles encontrados: " + proyectos.size());
        proyectos.forEach(p -> System.out.println("Proyecto disponible: " + p.getTitulo()));
        return proyectos;
    }

    @Transactional(readOnly = true)
    public List<Proyecto> obtenerProyectosPostuladosPorEstudiante(Estudiante estudiante) {
        if (estudiante == null) {
            return Collections.emptyList();
        }
        return proyectoRepository.findBySolicitudes_EstudianteAndSolicitudes_Estado(estudiante, Solicitud.EstadoSolicitud.PENDIENTE);
    }

    @Transactional
    public void inscribirEstudianteEnProyecto(String titulo, Long cuit, Estudiante estudiante) {
        Proyecto proyecto = proyectoRepository.findByProyectoId_TituloAndProyectoId_CuitEntidad(titulo, cuit);
        if (proyecto == null) {
            throw new RuntimeException("Proyecto no encontrado");
        }

        if (proyecto.getEstado() != Proyecto.EstadoProyecto.EN_ESPERA) {
            throw new RuntimeException("El proyecto no está disponible");
        }

        Solicitud solicitud = new Solicitud();
        solicitud.setProyecto(proyecto);
        solicitud.setSolicitante(estudiante);
        solicitud.setEstado(Solicitud.EstadoSolicitud.PENDIENTE);
        solicitud.setFechaSolicitud(LocalDateTime.now());

        solicitudRepository.save(solicitud);
    }

    @Transactional
    public void cancelarSolicitud(Long solicitudId, Estudiante estudiante) {
        Solicitud solicitud = solicitudRepository.findById(solicitudId)
            .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));

        if (!solicitud.getSolicitante().getId().equals(estudiante.getId())) {
            throw new RuntimeException("No tienes permiso para cancelar esta solicitud");
        }

        if (solicitud.getEstado() != Solicitud.EstadoSolicitud.PENDIENTE) {
            throw new RuntimeException("Solo se pueden cancelar solicitudes pendientes");
        }

        solicitudRepository.delete(solicitud);
    }

    //Calcular progreso de un proyecto
    public double calcularProgreso(Proyecto proyecto) {
        if (proyecto == null || proyecto.getPlanDeTrabajo() == null) {
            return 0.0;
        }

        List<Actividad> actividades = proyecto.getPlanDeTrabajo().getActividades();
        if (actividades == null || actividades.isEmpty()) {
            return 0.0;
        }

        int horasTotales = actividades.stream()
                .mapToInt(Actividad::getCantidadHoras)
                .sum();

        int horasCompletadas = actividades.stream()
                .filter(a -> a.getEstado() == Actividad.EstadoActividad.COMPLETADA)
                .mapToInt(Actividad::getCantidadHoras)
                .sum();

        return (double) horasCompletadas / horasTotales * 100;
    }
}
