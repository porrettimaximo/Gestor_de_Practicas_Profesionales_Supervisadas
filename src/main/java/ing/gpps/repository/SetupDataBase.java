package ing.gpps.repository;

import ing.gpps.entity.institucional.*;
import ing.gpps.entity.users.*;
import ing.gpps.service.EntidadService;
import ing.gpps.service.EstudianteService;
import ing.gpps.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import ing.gpps.entity.institucional.TipoEntidad;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
    private AreaRepository areaRepository;
    private final NotificacionRepository notificacionRepository;
    private final TutorRepository tutorRepository;
    private final ActividadRepository actividadRepository;

    @Autowired
    public SetupDataBase(UsuarioRepository usuarioRepository, UsuarioService usuarioService,
                         ProyectoRepository proyectoRepository, EntregaRepository entregaRepository,
                         EntidadRepository entidadRepository, PlanDeTrabajoRepository planDeTrabajoRepository,
                         EntidadService entidadService,
                         AreaRepository areaRepository, EstudianteService estudianteService, NotificacionRepository notificacionRepository,
                         TutorRepository tutorRepository, ActividadRepository actividadRepository) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioService = usuarioService;
        this.proyectoRepository = proyectoRepository;
        this.entregaRepository = entregaRepository;
        this.entidadRepository = entidadRepository;
        this.planDeTrabajoRepository = planDeTrabajoRepository;
        this.estudianteService = estudianteService;
        this.entidadService = entidadService;
        this.notificacionRepository = notificacionRepository;
        this.tutorRepository = tutorRepository;
        this.actividadRepository = actividadRepository;
        this.areaRepository = areaRepository;
    }

    // Asegurar que el usuario porrettimaxi tiene un proyecto asignado
    private void cargarDatos() {
        // Crear usuarios
        Estudiante estudiante1 = new Estudiante("Lautaro", "Salvo", "salvoschaferlautaro@gmail.com", "1234", 42658278L, 1521L, 2920219900L);
        Estudiante estudiante2 = new Estudiante("Maximo", "Porretti", "porretimaxi@gmail.com", "2345", 46456214L, 1841L, 2920223500L);
        Estudiante estudiante3 = new Estudiante("Tomas", "Acosta", "acostatomas@gmail.com", "3456", 45234765L, 4526L, 2920652378L);
        Estudiante estudiante4 = new Estudiante("Cristian", "Millaqueo", "cristianmillaqueo.12ok@gmail.com", "9293", 436808L, 4521L, 2944929339L);

        Admin admin1 = new Admin("Admin", "Admin", "admin@gmail.com", "admin", 2920123456L);

        // Registrar usuarios solo si no existen
        if (!usuarioRepository.existsByEmail(estudiante1.getEmail())) {
            usuarioService.registrarUsuario(estudiante1);
        }
        if (!usuarioRepository.existsByEmail(estudiante2.getEmail())) {
            usuarioService.registrarUsuario(estudiante2);
        }
        if (!usuarioRepository.existsByEmail(estudiante3.getEmail())) {
            usuarioService.registrarUsuario(estudiante3);
        }
        if (!usuarioRepository.existsByEmail(estudiante4.getEmail())) {
            usuarioService.registrarUsuario(estudiante4);
        }
        if (!usuarioRepository.existsByEmail(admin1.getEmail())) {
            usuarioService.registrarUsuario(admin1);
        }

        // Obtener instancias gestionadas de estudiantes
        Estudiante managedEstudiante1 = estudianteService.buscarPorEmail(estudiante1.getEmail()).orElseThrow(() -> new RuntimeException("Estudiante 1 no encontrado"));
        Estudiante managedEstudiante2 = estudianteService.buscarPorEmail(estudiante2.getEmail()).orElseThrow(() -> new RuntimeException("Estudiante 2 no encontrado"));
        Estudiante managedEstudiante3 = estudianteService.buscarPorEmail(estudiante3.getEmail()).orElseThrow(() -> new RuntimeException("Estudiante 3 no encontrado"));

        // Crear tutores
        DocenteSupervisor tutorUNRN = new DocenteSupervisor("María", "González", "maria_gonzalez@unrn.edu.ar", "tutor123", 2920123456L);
        TutorExterno tutorExterno = new TutorExterno("Juan", "Pérez", "juan_perez@empresa.com", "tutor456", 2920654321L);
        DocenteSupervisor tutorUNRN2 = new DocenteSupervisor("Carlos", "Rodríguez", "carlos_rodriguez@unrn.edu.ar", "tutor789", 2920789456L);
        TutorExterno tutorExterno0 = new TutorExterno("Ana", "Martínez", "ana_martinez@empresa2.com", "tutor101", 2920456789L);

        // Registrar tutores solo si no existen
        if (!usuarioRepository.existsByEmail(tutorUNRN.getEmail())) {
            usuarioService.registrarUsuario(tutorUNRN);
        }
        if (!usuarioRepository.existsByEmail(tutorExterno.getEmail())) {
            usuarioService.registrarUsuario(tutorExterno);
        }
        if (!usuarioRepository.existsByEmail(tutorUNRN2.getEmail())) {
            usuarioService.registrarUsuario(tutorUNRN2);
        }
        if (!usuarioRepository.existsByEmail(tutorExterno0.getEmail())) {
            usuarioService.registrarUsuario(tutorExterno0);
        }

        // Crear entidades
        Entidad entidad1 = new Entidad(12345678L, "Empresa Altec", "Viedma", "altec@unrn.com", TipoEntidad.EMPRESA, "2920123456");
        Entidad entidad2 = new Entidad(87654321L, "Municipalidad de Viedma", "Viedma", "municipalidad@viedma.gov.ar", TipoEntidad.ORGANISMO_PUBLICO, "2920987654");
        Entidad entidad3 = new Entidad(98765432L, "Hospital Zatti", "Viedma", "contacto@hospitalzatti.com", TipoEntidad.INSTITUCION_SALUD, "2920765432");

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
                tutorExterno0,
                entidad2
        );

        Proyecto proyecto3 = new Proyecto(
                "Plataforma de telemedicina",
                "Implementación de una plataforma de telemedicina para consultas remotas y seguimiento de pacientes.",
                null,  // Primero creamos el proyecto sin estudiante
                tutorUNRN,
                tutorExterno0,
                entidad3
        );

        Area area0 = new Area("Desarrollo de Software");
        areaRepository.save(area0); // Guardar el área en la base de datos

        proyecto1.setArea(area0);

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
                planDeTrabajo1, 100
        );
        actividad1.setFechaLimite(LocalDate.of(2025, 4, 30));

        Actividad actividad2 = new Actividad(
                2,
                "Diseño de Arquitectura",
                "Diseñar la arquitectura del sistema, incluyendo diagramas de clases, secuencia y componentes.",
                planDeTrabajo1, 50
        );
        actividad2.setFechaLimite(LocalDate.of(2025, 5, 15));

        Actividad actividad3 = new Actividad(
                1,
                "Investigación de Necesidades",
                "Realizar entrevistas y encuestas para identificar las necesidades específicas de los ciudadanos.",
                planDeTrabajo2, 25
        );
        actividad3.setFechaLimite(LocalDate.of(2025, 5, 15));

        Actividad actividad4 = new Actividad(
                1,
                "Análisis de Requisitos Técnicos",
                "Analizar los requisitos técnicos para la implementación de la plataforma de telemedicina.",
                planDeTrabajo3, 25
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

        // Crear una nueva entidad distinta
        Entidad nuevaEntidad = new Entidad(
                98765432L, // CUIT distinto
                "Fundación Patagonia Tec",
                "Cipolletti",
                "fundacionpatagonia@gmail.com",
                TipoEntidad.ONG,
                "2984123456"
        );
        entidadService.registrarEntidad(nuevaEntidad);

        Proyecto p = new Proyecto("Desarrollo IA",
                "Desarrollo de un sistema de IA para optimización de procesos industriales.",
                null,
                null,
                tutorExterno,
                nuevaEntidad
        );

        proyectoRepository.save(p);

        //Crea cinco áreas distintas
        List<Proyecto> proyectosArea1 = new ArrayList<>();
        List<Proyecto> proyectosArea2 = new ArrayList<>();
        List<Proyecto> proyectosArea3 = new ArrayList<>();
        List<Proyecto> proyectosArea4 = new ArrayList<>();
        List<Proyecto> proyectosArea5 = new ArrayList<>();

        Area area1 = new Area("Desarrollo web", proyectosArea1);
        Area area2 = new Area("Desarrollo móvil", proyectosArea2);
        Area area3 = new Area("Inteligencia Artificial", proyectosArea3);
        Area area4 = new Area("Ciberseguridad", proyectosArea4);
        Area area5 = new Area("Big Data", proyectosArea5);

        areaRepository.save(area1);
        areaRepository.save(area2);
        areaRepository.save(area3);
        areaRepository.save(area4);
        areaRepository.save(area5);

        AdminEntidad adminEntidad = new AdminEntidad(
                "Cristian",
                "Millaqueo",
                "cristianmillaqueo@gmail.com",
                "1234",
                2984123456L
        );
        adminEntidad.setEntidad(nuevaEntidad);

        //define dos tutores externos para la entidad
        TutorExterno tutorExterno1 = new TutorExterno("Ana", "López", "ana@gmail.com", "tutorAna", 2987654321L);
        TutorExterno tutorExterno2 = new TutorExterno("Carlos", "Martínez", "martinezcarlos@gmail.com", "carlitos123", 2988765432L);
        // Asignar los tutores externos a la entidad
        tutorExterno1.setEntidad(nuevaEntidad);
        tutorExterno2.setEntidad(nuevaEntidad);
        tutorRepository.save(tutorExterno1);
        tutorRepository.save(tutorExterno2);

        usuarioService.registrarUsuario(adminEntidad);

        System.out.println("Administrador de entidad cargado: " + adminEntidad.getNombre() + " (" + adminEntidad.getEmail() + ")");


        System.out.println("Datos cargados correctamente");
        System.out.println("Estudiantes registrados: " + usuarioRepository.count());
        System.out.println("Proyectos creados: " + proyectoRepository.count());
        System.out.println("Entidades registradas: " + entidadRepository.count());
        System.out.println("Planes de trabajo creados: " + planDeTrabajoRepository.count());
        System.out.println("Entregas registradas: " + entregaRepository.count());
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        cargarDatos();
    }
}
