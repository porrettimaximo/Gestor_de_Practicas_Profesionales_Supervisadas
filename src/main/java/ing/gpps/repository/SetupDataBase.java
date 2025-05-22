package ing.gpps.repository;

import ing.gpps.entity.pps.Entrega;
import ing.gpps.entity.pps.Proyecto;
import ing.gpps.entity.users.*;
import ing.gpps.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

public class SetupDataBase {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioService usuarioService;
    private final ProyectoRepository proyectoRepository;
    private final EntregaRepository entregaRepository;

    @Autowired
    public SetupDataBase(UsuarioRepository usuarioRepository, UsuarioService usuarioService,
                         ProyectoRepository proyectoRepository, EntregaRepository entregaRepository) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioService = usuarioService;
        this.proyectoRepository = proyectoRepository;
        this.entregaRepository = entregaRepository;
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

        // Crear proyectos con una descripción más corta
        Proyecto proyecto1 = new Proyecto(
                "Desarrollo de aplicación de ventas",
                "Desarrollo de una aplicación web para gestión de inventario y ventas. Incluye interfaz intuitiva para seguimiento de productos, gestión de ventas y generación de informes.",
                LocalDate.of(2025, 3, 15),
                LocalDate.of(2025, 6, 15),
                estudiante2, // Maximo Porreti
                tutorUNRN,
                tutorExterno,
                "Empresa Altec"
        );

        // Agregar objetivos
        proyecto1.addObjetivo("Desarrollar una interfaz de usuario intuitiva y responsive.");
        proyecto1.addObjetivo("Implementar un sistema de gestión de inventario con alertas de stock.");
        proyecto1.addObjetivo("Crear un módulo de ventas con generación de facturas.");
        proyecto1.addObjetivo("Desarrollar un panel de administración para la gestión de usuarios y permisos.");
        proyecto1.addObjetivo("Implementar un sistema de reportes y estadísticas.");

        proyecto1.setProgreso(75); // Establecer progreso
        proyectoRepository.save(proyecto1);

        // Crear entregas para el proyecto
        Entrega entrega1 = new Entrega(
                "Entrega 1: Análisis de Requerimientos",
                "Documento con el análisis detallado de los requerimientos del sistema",
                LocalDate.of(2025, 4, 30),
                proyecto1
        );

        Entrega entrega2 = new Entrega(
                "Entrega 2: Diseño de Arquitectura",
                "Documento con el diseño de la arquitectura del sistema",
                LocalDate.of(2025, 5, 15),
                proyecto1
        );

        Entrega entrega3 = new Entrega(
                "Entrega 3: Implementación del Módulo de Ventas",
                "Código fuente y documentación del módulo de ventas",
                LocalDate.of(2025, 6, 1),
                proyecto1
        );

        // Guardar entregas
        entregaRepository.save(entrega1);
        entregaRepository.save(entrega2);
        entregaRepository.save(entrega3);

        // Simular una entrega ya realizada y aprobada
        entrega1.setEstado(Entrega.EstadoEntrega.APROBADO);
        entrega1.setFechaEntrega(LocalDate.of(2025, 4, 22));
        entrega1.setTamanoArchivo("45 KB");
        entregaRepository.save(entrega1);

        System.out.println("Datos cargados correctamente");
        System.out.println("Estudiante: " + estudiante2.getNombre() + " " + estudiante2.getApellido() + " con email: " + estudiante2.getEmail());
        System.out.println("Proyecto asignado: " + proyecto1.getTitulo());
        System.out.println("entregas: " + proyecto1.getEntregas());
        System.out.println("Número de entregas: " + proyecto1.getEntregas().size());
    }
}
