package ing.gpps;

import ing.gpps.entity.users.Estudiante;
import ing.gpps.repository.*;
import ing.gpps.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DemoApplication {

	private static final Logger logger = LoggerFactory.getLogger(DemoApplication.class);

	private final UsuarioRepository usuarioRepository;
	private final UsuarioService usuarioService;
	private final ProyectoRepository proyectoRepository;
	private final EntregaRepository entregaRepository;
	private final EntidadRepository entidadRepository;
	private final EntidadService entidadService;
	private final PlanDeTrabajoRepository planDeTrabajoRepository;
	private final ActividadRepository actividadRepository;
	private final ActividadService actividadService;
	private final InformeRepository informeRepository;
	private final InformeService informeService;
	private final EstudianteRepository estudianteRepository;
	private final EstudianteService estudianteService;
    private final AreaRepository areaRepository;
    private final NotificacionRepository notificacionRepository;
    private final TutorRepository tutorRepository;

	@Autowired
	public DemoApplication(UsuarioRepository usuarioRepository,
						 UsuarioService usuarioService,
						 ProyectoRepository proyectoRepository,
						 EntregaRepository entregaRepository,
						 EntidadRepository entidadRepository,
						 EntidadService entidadService,
						 PlanDeTrabajoRepository planDeTrabajoRepository,
						 ActividadRepository actividadRepository,
						 ActividadService actividadService,
						 InformeRepository informeRepository,
						 InformeService informeService,
						 EstudianteRepository estudianteRepository,
						 EstudianteService estudianteService,
                           AreaRepository areaRepository,
                           NotificacionRepository notificacionRepository,
                           TutorRepository tutorRepository) {
		this.usuarioRepository = usuarioRepository;
		this.usuarioService = usuarioService;
		this.proyectoRepository = proyectoRepository;
		this.entregaRepository = entregaRepository;
		this.entidadRepository = entidadRepository;
		this.entidadService = entidadService;
		this.planDeTrabajoRepository = planDeTrabajoRepository;
		this.actividadRepository = actividadRepository;
		this.actividadService = actividadService;
		this.informeRepository = informeRepository;
		this.informeService = informeService;
		this.estudianteRepository = estudianteRepository;
		this.estudianteService = estudianteService;
        this.areaRepository = areaRepository;
        this.notificacionRepository = notificacionRepository;
        this.tutorRepository = tutorRepository;
	}

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
		return args -> {
			logger.info("Iniciando aplicación GPPS...");
			logger.info("Número de beans cargados: {}", ctx.getBeanDefinitionCount());

			// Crear instancia de SetupDataBase
			SetupDataBase setupDataBase = new SetupDataBase(
				usuarioRepository,
				usuarioService,
				proyectoRepository,
				entregaRepository,
				entidadRepository,
				planDeTrabajoRepository,
				entidadService,
					areaRepository,
				estudianteService,
                    notificacionRepository,
					tutorRepository,
					actividadRepository
			);

			// Verificar datos cargados
			logger.info("=== Estado de la base de datos ===");
			logger.info("Usuarios registrados: {}", usuarioRepository.count());
			logger.info("Proyectos creados: {}", proyectoRepository.count());
			logger.info("Entidades registradas: {}", entidadRepository.count());
			logger.info("Planes de trabajo: {}", planDeTrabajoRepository.count());
			logger.info("Actividades: {}", actividadRepository.count());
			logger.info("Entregas: {}", entregaRepository.count());
			logger.info("Informes: {}", informeRepository.count());
			logger.info("================================");
		};
	}
}
