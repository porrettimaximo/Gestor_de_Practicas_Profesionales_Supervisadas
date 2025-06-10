package ing.gpps.repository;

import ing.gpps.entity.institucional.*;
import ing.gpps.entity.users.*;
import ing.gpps.service.EntidadService;
import ing.gpps.service.*;
import ing.gpps.service.EstudianteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import ing.gpps.entity.institucional.TipoEntidad;

@Component
public class SetupDataBase implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioService usuarioService;
    private final ProyectoRepository proyectoRepository;
    private final EntregaRepository entregaRepository;
    private final EntidadRepository entidadRepository;
    private final PlanDeTrabajoRepository planDeTrabajoRepository;
    private final EntidadService entidadService;
    private final EstudianteService estudianteService;
    private final AreaRepository areaRepository;
    private final NotificacionRepository notificacionRepository;

    @Autowired
    public SetupDataBase(UsuarioRepository usuarioRepository, UsuarioService usuarioService,
                         ProyectoRepository proyectoRepository, EntregaRepository entregaRepository,
                         EntidadRepository entidadRepository, PlanDeTrabajoRepository planDeTrabajoRepository,
                         EntidadService entidadService,
                         AreaRepository areaRepository, NotificacionRepository notificacionRepository) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioService = usuarioService;
        this.proyectoRepository = proyectoRepository;
        this.entregaRepository = entregaRepository;
        this.entidadRepository = entidadRepository;
        this.planDeTrabajoRepository = planDeTrabajoRepository;
        this.entidadService = entidadService;
        this.estudianteService = estudianteService;
        this.areaRepository = areaRepository;
        this.notificacionRepository = notificacionRepository;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        this.areaRepository = areaRepository;
        cargarDatos();
    }

    private void cargarDatos() {
        // Crear usuarios
        Estudiante estudiante1 = new Estudiante("Lautaro", "Salvo", "salvoschaferlautaro@gmail.com", "1234", 42658278L, 1521L, 2920219900L);
        Estudiante estudiante2 = new Estudiante("Maximo", "Porretti", "porretimaxi@gmail.com", "2345", 46456214L, 1841L, 2920223500L);
        Estudiante estudiante3 = new Estudiante("Tomas", "Acosta", "acostatomas@gmail.com", "3456", 45234765L, 4526L, 2920652378L);
        Estudiante estudiante4 = new Estudiante("Cristian", "Millaqueo", "cristianmillaqueo.12ok@gmail.com", "9293", 436808L, 4521L, 2944929339L);


        Admin admin1 = new Admin("Admin", "Admin", "admin@gmail.com", "admin", 2920123456L);

        usuarioService.registrarUsuario(estudiante1);
        usuarioService.registrarUsuario(estudiante2);
        usuarioService.registrarUsuario(estudiante3);
        usuarioService.registrarUsuario(estudiante4);

        usuarioService.registrarUsuario(admin1);

        // Obtener instancias gestionadas de estudiantes
        Estudiante managedEstudiante1 = estudianteService.buscarPorEmail(estudiante1.getEmail()).orElseThrow(() -> new RuntimeException("Estudiante 1 no encontrado"));
        Estudiante managedEstudiante2 = estudianteService.buscarPorEmail(estudiante2.getEmail()).orElseThrow(() -> new RuntimeException("Estudiante 2 no encontrado"));
        Estudiante managedEstudiante3 = estudianteService.buscarPorEmail(estudiante3.getEmail()).orElseThrow(() -> new RuntimeException("Estudiante 3 no encontrado"));

        // Crear tutores
        DocenteSupervisor tutorUNRN = new DocenteSupervisor("María", "González", "maria_gonzalez@unrn.edu.ar", "tutor123", 2920123456L);
        TutorExterno tutorExterno = new TutorExterno("Juan", "Pérez", "juan_perez@empresa.com", "tutor456", 2920654321L);
        DocenteSupervisor tutorUNRN2 = new DocenteSupervisor("Carlos", "Rodríguez", "carlos_rodriguez@unrn.edu.ar", "tutor789", 2920789456L);
        TutorExterno tutorExterno2 = new TutorExterno("Ana", "Martínez", "ana_martinez@empresa2.com", "tutor101", 2920456789L);

        usuarioService.registrarUsuario(tutorUNRN);
        usuarioService.registrarUsuario(tutorExterno);
        usuarioService.registrarUsuario(tutorUNRN2);
        usuarioService.registrarUsuario(tutorExterno2);

        // Crear entidades
        Entidad entidad1 = new Entidad(12345678L, "Empresa Altec", "Viedma", "altec@unrn.com", TipoEntidad.EMPRESA);
        Entidad entidad2 = new Entidad(87654321L, "Municipalidad de Viedma", "Viedma", "municipalidad@viedma.gov.ar", TipoEntidad.ORGANISMO_PUBLICO);
        Entidad entidad3 = new Entidad(98765432L, "Hospital Zatti", "Viedma", "contacto@hospitalzatti.com", TipoEntidad.INSTITUCION_SALUD);

        entidadService.registrarEntidad(entidad1);
        entidadService.registrarEntidad(entidad2);
        entidadService.registrarEntidad(entidad3);

        // Crear proyectos
        Proyecto proyecto1 = new Proyecto(
                "Desarrollo de aplicación de ventas",
                "Desarrollo de una aplicación web para gestión de inventario y ventas. Incluye interfaz intuitiva para seguimiento de productos, gestión de ventas y generación de informes.",
                null,  // Primero creamos el proyecto sin estudiante
                tutorUNRN,
                tutorExterno,
                entidad1
        );

        Proyecto proyecto2 = new Proyecto(
                "Sistema de gestión municipal",
                "Desarrollo de un sistema integral para la gestión de trámites y servicios municipales, incluyendo módulos de atención al ciudadano y gestión interna.",
                null,  // Primero creamos el proyecto sin estudiante
                tutorUNRN2,
                tutorExterno2,
                entidad2
        );

        Proyecto proyecto3 = new Proyecto(
                "Plataforma de telemedicina",
                "Implementación de una plataforma de telemedicina para consultas remotas y seguimiento de pacientes.",
                null,  // Primero creamos el proyecto sin estudiante
                tutorUNRN,
                tutorExterno2,
                entidad3
        );

        Area area1 = new Area("Desarrollo de Software");
        areaRepository.save(area1); // Guardar el área en la base de datos

        proyecto1.setArea(area1);

        // Agregar objetivos
        // Guardar los proyectos primero
        proyectoRepository.save(proyecto1);
        proyectoRepository.save(proyecto2);
        proyectoRepository.save(proyecto3);

        // Ahora asignamos los estudiantes a los proyectos
        managedEstudiante2.asignarProyecto(proyecto1);
        managedEstudiante1.asignarProyecto(proyecto2);
        managedEstudiante3.asignarProyecto(proyecto3);

        // Guardar los estudiantes con sus proyectos asignados
        estudianteService.guardarEstudiante(managedEstudiante2);
        estudianteService.guardarEstudiante(managedEstudiante1);
        estudianteService.guardarEstudiante(managedEstudiante3);

        // Agregar objetivos a los proyectos
        proyecto1.addObjetivo("Desarrollar una interfaz de usuario intuitiva y responsive.");
        proyecto1.addObjetivo("Implementar un sistema de gestión de inventario con alertas de stock.");
        proyecto1.addObjetivo("Crear un módulo de ventas con generación de facturas.");
        proyecto1.addObjetivo("Desarrollar un panel de administración para la gestión de usuarios y permisos.");
        proyecto1.addObjetivo("Implementar un sistema de reportes y estadísticas.");

        proyecto2.addObjetivo("Diseñar e implementar el módulo de atención al ciudadano.");
        proyecto2.addObjetivo("Desarrollar el sistema de gestión de trámites.");
        proyecto2.addObjetivo("Crear el módulo de reportes y estadísticas municipales.");
        proyecto2.addObjetivo("Implementar el sistema de notificaciones.");

        proyecto3.addObjetivo("Desarrollar el módulo de videoconsultas.");
        proyecto3.addObjetivo("Implementar el sistema de historias clínicas digitales.");
        proyecto3.addObjetivo("Crear el módulo de seguimiento de pacientes.");
        proyecto3.addObjetivo("Desarrollar el sistema de agenda médica.");

        proyecto1.setProgreso(75);
        proyecto2.setProgreso(30);
        proyecto3.setProgreso(15);

        // Crear planes de trabajo
        PlanDeTrabajo planDeTrabajo1 = new PlanDeTrabajo(
                1,
                LocalDate.now(),
                LocalDate.of(2025, 12, 1),
                proyecto1
        );

        PlanDeTrabajo planDeTrabajo2 = new PlanDeTrabajo(
                1,
                LocalDate.now(),
                LocalDate.of(2025, 11, 30),
                proyecto2
        );

        PlanDeTrabajo planDeTrabajo3 = new PlanDeTrabajo(
                1,
                LocalDate.now(),
                LocalDate.of(2025, 10, 31),
                proyecto3
        );

        // Crear actividades para cada plan de trabajo
        Actividad actividad1 = new Actividad(
                1,
                "Análisis de Requerimientos",
                "Realizar un análisis detallado de los requerimientos del sistema, incluyendo entrevistas con usuarios y revisión de documentación existente.",
                planDeTrabajo1
        );
        actividad1.setFechaLimite(LocalDate.of(2025, 4, 30));

        Actividad actividad2 = new Actividad(
                2,
                "Diseño de Arquitectura",
                "Diseñar la arquitectura del sistema, incluyendo diagramas de clases, secuencia y componentes.",
                planDeTrabajo1
        );
        actividad2.setFechaLimite(LocalDate.of(2025, 5, 15));

        Actividad actividad3 = new Actividad(
                1,
                "Investigación de Necesidades",
                "Realizar entrevistas y encuestas para identificar las necesidades específicas de los ciudadanos.",
                planDeTrabajo2
        );
        actividad3.setFechaLimite(LocalDate.of(2025, 5, 15));

        Actividad actividad4 = new Actividad(
                1,
                "Análisis de Requisitos Técnicos",
                "Analizar los requisitos técnicos para la implementación de la plataforma de telemedicina.",
                planDeTrabajo3
        );
        actividad4.setFechaLimite(LocalDate.of(2025, 5, 20));

        planDeTrabajo1.addActividad(actividad1);
        planDeTrabajo1.addActividad(actividad2);
        planDeTrabajo2.addActividad(actividad3);
        planDeTrabajo3.addActividad(actividad4);

        // Crear entregas
        Entrega entrega1 = new Entrega(
                "Entrega 1: Análisis de Requerimientos",
                "Documento con el análisis detallado de los requerimientos del sistema",
                LocalDate.of(2025, 4, 30),
                actividad1
        );

        Entrega entrega2 = new Entrega(
                "Entrega 2: Diseño de Arquitectura",
                "Documento con el diseño de la arquitectura del sistema",
                LocalDate.of(2025, 5, 15),
                actividad2
        );

        Entrega entrega4 = new Entrega(
                "Entrega 1: Informe de Investigación",
                "Documento con los resultados de la investigación de necesidades",
                LocalDate.of(2025, 5, 15),
                actividad3
        );

        Entrega entrega5 = new Entrega(
                "Entrega 1: Especificación Técnica",
                "Documento con la especificación técnica del sistema",
                LocalDate.of(2025, 5, 20),
                actividad4
        );

        // Guardar planes de trabajo
        planDeTrabajoRepository.save(planDeTrabajo1);
        planDeTrabajoRepository.save(planDeTrabajo2);
        planDeTrabajoRepository.save(planDeTrabajo3);

        // Guardar entregas
        entregaRepository.save(entrega1);
        entregaRepository.save(entrega2);

        entregaRepository.save(entrega4);
        entregaRepository.save(entrega5);

        // Simular entregas realizadas y aprobadas
        entrega1.setEstado(Entrega.EstadoEntrega.APROBADO);
        entrega1.setFechaEntrega(LocalDate.of(2025, 4, 22));
        entregaRepository.save(entrega1);

        // Simular entregas realizadas y aprobadas
        entrega2.setEstado(Entrega.EstadoEntrega.ENTREGADO);
        entrega2.setFechaEntrega(LocalDate.of(2025, 4, 22));
        entregaRepository.save(entrega2);

        System.out.println("Datos cargados correctamente");
        System.out.println("Estudiantes registrados: " + usuarioRepository.count());
        System.out.println("Proyectos creados: " + proyectoRepository.count());
        System.out.println("Entidades registradas: " + entidadRepository.count());
        System.out.println("Planes de trabajo creados: " + planDeTrabajoRepository.count());
        System.out.println("Entregas registradas: " + entregaRepository.count());
    }
}
