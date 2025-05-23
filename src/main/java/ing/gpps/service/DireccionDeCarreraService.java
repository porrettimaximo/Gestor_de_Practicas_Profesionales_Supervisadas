package ing.gpps.service;

import ing.gpps.entity.institucional.Proyecto;
import ing.gpps.entity.users.Estudiante;
import ing.gpps.repository.ProyectoRepository;
import ing.gpps.repository.UsuarioRepository;

public class DireccionDeCarreraService {

    private final ProyectoRepository proyectoRepository;
    private final UsuarioRepository usuarioRepository;
    private final GenerarConvenioService generarConvenioService;

    public DireccionDeCarreraService(ProyectoRepository proyectoRepository, UsuarioRepository usuarioRepository, GenerarConvenioService generarConvenioService){
        this.proyectoRepository = proyectoRepository;
        this.usuarioRepository = usuarioRepository;
        this.generarConvenioService = generarConvenioService;
    }


    public void aprobarEstudiantePPS(Proyecto p, Estudiante e) {

//        p.asignarEstudiante(e);
//        e.asignarProyecto(p);
//        p.setEstado(Proyecto.EstadoProyecto.EN_CURSO);
//        p.setProgreso(0);
//        p.setFechaInicio(java.time.LocalDate.now());
//        p.setFechaFinEstimada(java.time.LocalDate.now().plusMonths(6));
//
//        proyectoRepository.save(p);
//
//        // Enviar notificación al estudiante
//        generarConvenioService.generar(p, e);




    }



}
