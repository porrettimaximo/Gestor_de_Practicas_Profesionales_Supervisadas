package ing.gpps.repository;

import ing.gpps.entity.institucional.*;
import ing.gpps.entity.users.*;
import ing.gpps.service.EntidadService;
import ing.gpps.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SetupDataBase {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioService usuarioService;
    private final ProyectoRepository proyectoRepository;
    private final EntregaRepository entregaRepository;
    private final EntidadRepository entidadRepository;
    private final PlanDeTrabajoRepository planDeTrabajoRepository;
    private final EntidadService entidadService;
    private final AreaRepository areaRepository;
    private final TutorRepository tutorRepository;
    private final ActividadRepository actividadRepository;

    @Autowired
    public SetupDataBase(UsuarioRepository usuarioRepository, UsuarioService usuarioService,
                         ProyectoRepository proyectoRepository, EntregaRepository entregaRepository,
                         EntidadRepository entidadRepository, PlanDeTrabajoRepository planDeTrabajoRepository,
                         EntidadService entidadService, AreaRepository areaRepository, TutorRepository tutorRepository, ActividadRepository actividadRepository) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioService = usuarioService;
        this.proyectoRepository = proyectoRepository;
        this.entregaRepository = entregaRepository;
        this.entidadRepository = entidadRepository;
        this.planDeTrabajoRepository = planDeTrabajoRepository;
        this.entidadService = entidadService;
        this.areaRepository = areaRepository;
        this.tutorRepository = tutorRepository;
        this.actividadRepository = actividadRepository;
        cargarDatos();
    }

    // Asegurar que el usuario porrettimaxi tiene un proyecto asignado
    private void cargarDatos() {
        // Crear usuarios
        Estudiante estudiante1 = new Estudiante("Lautaro", "Salvo", "salvoschaferlautaro@gmail.com", "1234", 42658278L, 1521L, 2920219900L);
        Estudiante estudiante2 = new Estudiante("Maximo", "Porretti", "porretimaxi@gmail.com", "2345", 46456214L, 1841L, 2920223500L);
        Estudiante estudiante3 = new Estudiante("Tomas", "Acosta", "acostatomas@gmail.com", "3456", 45234765L, 4526L, 2920652378L);

        Admin admin1 = new Admin("Admin", "Admin", "admin@gmail.com", "admin", 2920123456L);

        usuarioService.registrarUsuario(estudiante1);
        usuarioService.registrarUsuario(estudiante2);
        usuarioService.registrarUsuario(estudiante3);

        usuarioService.registrarUsuario(admin1);

        // Crear tutores
        DocenteSupervisor tutorUNRN = new DocenteSupervisor("María", "González", "maria_gonzalez@unrn.edu.ar", "tutor123", 2920123456L);
        TutorExterno tutorExterno = new TutorExterno("Juan", "Pérez", "juan_perez@empresa.com", "tutor456", 2920654321L);

        usuarioService.registrarUsuario(tutorUNRN);
        usuarioService.registrarUsuario(tutorExterno);

        Entidad entidad = new Entidad(12345678L, "Empresa Altec", "Viedma", "altec@unrn.com", TipoEntidad.EMPRESA, "2920123456");

        entidadService.registrarEntidad(entidad);

        // Crear proyectos con una descripción más corta
        Proyecto proyecto1 = new Proyecto(
                "Desarrollo de aplicación de ventas",
                "Desarrollo de una aplicación web para gestión de inventario y ventas. Incluye interfaz intuitiva para seguimiento de productos, gestión de ventas y generación de informes.",
                estudiante2, // Maximo Porreti
                tutorUNRN,
                tutorExterno,
                entidad
        );

        // Agregar objetivos
        proyecto1.addObjetivo("Desarrollar una interfaz de usuario intuitiva y responsive.");
        proyecto1.addObjetivo("Implementar un sistema de gestión de inventario con alertas de stock.");
        proyecto1.addObjetivo("Crear un módulo de ventas con generación de facturas.");
        proyecto1.addObjetivo("Desarrollar un panel de administración para la gestión de usuarios y permisos.");
        proyecto1.addObjetivo("Implementar un sistema de reportes y estadísticas.");

        proyecto1.setProgreso(75); // Establecer progreso
        proyectoRepository.save(proyecto1);

        PlanDeTrabajo planDeTrabajo = new PlanDeTrabajo(
                1,
                LocalDate.now(),
                LocalDate.of(2025, 12, 1),
                proyecto1
        );

        Actividad actividad = new Actividad(
                1,
                "Análisis de Requerimientos",
                "Realizar un análisis detallado de los requerimientos del sistema, incluyendo entrevistas con usuarios y revisión de documentación existente.",
                true,
                planDeTrabajo,
                40 // Cantidad de horas estimadas para la actividad
        );

        planDeTrabajo.setActividades(List.of(actividad));

        // Crear entregas para el proyecto
        Entrega entrega1 = new Entrega(
                "Entrega 1: Análisis de Requerimientos",
                "Documento con el análisis detallado de los requerimientos del sistema",
                LocalDate.of(2025, 4, 30),
                planDeTrabajo.getActividades().get(0)
        );

        Entrega entrega2 = new Entrega(
                "Entrega 2: Diseño de Arquitectura",
                "Documento con el diseño de la arquitectura del sistema",
                LocalDate.of(2025, 5, 15),
                planDeTrabajo.getActividades().get(0)
        );

        Entrega entrega3 = new Entrega(
                "Entrega 3: Implementación del Módulo de Ventas",
                "Código fuente y documentación del módulo de ventas",
                LocalDate.of(2025, 6, 1),
                planDeTrabajo.getActividades().get(0)

        );

        planDeTrabajoRepository.save(planDeTrabajo);

        // Guardar entregas
        entregaRepository.save(entrega1);
        entregaRepository.save(entrega2);
        entregaRepository.save(entrega3);

        // Simular una entrega ya realizada y aprobada
        entrega1.setEstado(Entrega.EstadoEntrega.APROBADO);
        entrega1.setFechaEntrega(LocalDate.of(2025, 4, 22));
        entrega1.setTamanoArchivo("45 KB");
        entregaRepository.save(entrega1);

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
        System.out.println("Estudiante: " + estudiante2.getNombre() + " " + estudiante2.getApellido() + " con email: " + estudiante2.getEmail());
        System.out.println("Proyecto asignado: " + proyecto1.getTitulo());
        System.out.println("entregas: " + proyecto1.getPlanDeTrabajo().getActividades().getFirst().getEntregas());
        System.out.println("Número de entregas: " + proyecto1.getPlanDeTrabajo().getActividades().getFirst().getEntregas().size());


    }
}
