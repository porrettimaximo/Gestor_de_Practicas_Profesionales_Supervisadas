package ing.gpps.controller;

import ing.gpps.entity.institucional.*;
import ing.gpps.entity.users.Estudiante;
import ing.gpps.entity.users.Usuario;
import ing.gpps.entity.idClasses.ActividadId;
import ing.gpps.entity.idClasses.PlanDeTrabajoId;
import ing.gpps.entity.idClasses.ProyectoId;
import ing.gpps.security.CustomUserDetails;
import ing.gpps.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Collections;
import java.util.Map;

import static java.lang.Integer.valueOf;

import org.springframework.beans.factory.annotation.Value;

@Controller
@RequestMapping("/estudiante")
public class EstudianteController {
    private final UsuarioService usuarioService;
    private final ProyectoService proyectoService;
    private final EntregaService entregaService;
    private final ActividadService actividadService;
    private final InformeService informeService;
    private final FileStorageService fileStorageService;
    private final EstudianteService estudianteService;

    private static final Logger logger = LoggerFactory.getLogger(EstudianteController.class);

    @Value("${upload.path}")
    private String uploadPath;

    @Autowired
    public EstudianteController(UsuarioService usuarioService, 
                              ProyectoService proyectoService, 
                              EntregaService entregaService,
                              ActividadService actividadService,
                              InformeService informeService,
                              FileStorageService fileStorageService,
                              EstudianteService estudianteService) {
        this.usuarioService = usuarioService;
        this.proyectoService = proyectoService;
        this.entregaService = entregaService;
        this.actividadService = actividadService;
        this.informeService = informeService;
        this.fileStorageService = fileStorageService;
        this.estudianteService = estudianteService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        Authentication authentication = null;
        try {
            authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()) {
                Object principal = authentication.getPrincipal();

                if (principal instanceof CustomUserDetails) {
                    CustomUserDetails userDetails = (CustomUserDetails) principal;
                    Usuario usuario = userDetails.getUsuario();

                    if (usuario instanceof Estudiante) {
                        Estudiante estudiante = (Estudiante) usuario;
                        model.addAttribute("estudiante", estudiante);

                        List<Proyecto> proyectos = estudianteService.obtenerProyectosPorEstudiante(estudiante);

                        if (proyectos != null && !proyectos.isEmpty()) {
                            Proyecto proyectoActual = proyectos.get(0);
                            model.addAttribute("proyecto", proyectoActual);

                            // Verificar si el docente supervisor es nulo
                            if (proyectoActual.getTutorUNRN() == null) {
                                logger.warn("El proyecto del estudiante {} {} (ID: {}) no tiene un Docente Supervisor asignado.", 
                                            estudiante.getNombre(), estudiante.getApellido(), estudiante.getId());
                            }

                            PlanDeTrabajo planDeTrabajo = proyectoActual.getPlanDeTrabajo();
                            if (planDeTrabajo != null) {
                                model.addAttribute("planDeTrabajo", planDeTrabajo);

                                logger.info("PlanDeTrabajo no es nulo. Intentando obtener actividades para el plan de trabajo ID: {}", planDeTrabajo.getPlanDeTrabajoId());
                                List<Actividad> actividades = actividadService.obtenerActividadesPorPlanDeTrabajo(planDeTrabajo);
                                logger.info("Resultado de obtenerActividadesPorPlanDeTrabajo: {}", actividades != null ? "Lista (tamaño: " + actividades.size() + ")" : "null");

                                model.addAttribute("actividades", actividades != null ? actividades : Collections.emptyList());

                                logger.info("Intentando obtener entregas para el proyecto ID: {}", proyectoActual.getProyectoId());
                                List<Entrega> entregas = entregaService.buscarPorProyecto(proyectoActual);
                                logger.info("Resultado de buscarPorProyecto (Entregas): {}", entregas != null ? "Lista (tamaño: " + entregas.size() + ")" : "null");

                                model.addAttribute("entregas", entregas != null ? entregas : Collections.emptyList());

                                logger.info("Intentando obtener informes para el estudiante ID: {}", estudiante.getId());
                                List<Informe> informes = estudianteService.obtenerInformesPorEstudiante(valueOf(estudiante.getId()));
                                logger.info("Resultado de obtenerInformesPorEstudiante: {}", informes != null ? "Lista (tamaño: " + informes.size() + ")" : "null");

                                model.addAttribute("informes", informes != null ? informes : Collections.emptyList());

                                logger.info("Cargando dashboard para estudiante: {} {}", estudiante.getNombre(), estudiante.getApellido());
                                logger.info("Proyecto: {}", proyectoActual.getTitulo());
                                logger.info("Número de actividades (final): {}", actividades != null ? actividades.size() : 0);
                                logger.info("Número de entregas (final): {}", entregas != null ? entregas.size() : 0);
                                logger.info("Número de informes (final): {}", informes != null ? informes.size() : 0);

                                return "indexAlumno";
                            } else {
                                logger.warn("El proyecto del estudiante {} {} (ID: {}) no tiene un plan de trabajo asignado.", 
                                            estudiante.getNombre(), estudiante.getApellido(), estudiante.getId());
                                model.addAttribute("planDeTrabajo", null);
                                model.addAttribute("actividades", Collections.emptyList());
                                model.addAttribute("entregas", Collections.emptyList());
                                model.addAttribute("informes", Collections.emptyList());
                                return "indexAlumno";
                            }
                        } else {
                            logger.warn("El estudiante no tiene proyectos asignados: {} {}", estudiante.getNombre(), estudiante.getApellido());
                            model.addAttribute("proyecto", null);
                            model.addAttribute("planDeTrabajo", null);
                            model.addAttribute("actividades", Collections.emptyList());
                            model.addAttribute("entregas", Collections.emptyList());
                            model.addAttribute("informes", Collections.emptyList());
                            return "indexAlumnoSinPPS";
                        }
                    }
                }
            }
        } catch (Exception e) {
            String username = (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails)
                              ? ((CustomUserDetails) authentication.getPrincipal()).getUsername() : "Desconocido";
            logger.error("Error en dashboard para estudiante {}: {}", username, e.getMessage(), e);
        }
        return "redirect:/login";
    }

    @PostMapping("/entregas/crear")
    @ResponseBody
    public String crearEntrega(@RequestBody Entrega entrega) {
        Authentication authentication = null;
        try {
            authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()) {
                CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
                Usuario usuario = userDetails.getUsuario();

                if (usuario instanceof Estudiante) {
                    Estudiante estudiante = (Estudiante) usuario;
                    entrega.setFecha(LocalDate.now());
                    entrega.setEstado(Entrega.EstadoEntrega.PENDIENTE);
                    entregaService.crearEntrega(entrega);
                    return "success";
                }
            }
            return "error";
        } catch (Exception e) {
            String username = (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails)
                             ? ((CustomUserDetails) authentication.getPrincipal()).getUsername() : "Desconocido";
            logger.error("Error al crear entrega para usuario {}: {}", username, e.getMessage());
            return "error";
        }
    }

    @PostMapping("/crearInforme")
    @Transactional
    public String crearInforme(@RequestParam("numero") int numero,
                              @RequestParam("titulo") String titulo,
                              @RequestParam("ruta") String ruta,
                              @RequestParam("actividadNumero") int actividadNumero,
                              @RequestParam("planNumero") int planNumero,
                              @RequestParam("proyectoTitulo") String proyectoTitulo,
                              @RequestParam("proyectoCuit") Long proyectoCuit,
                              Authentication authentication) {
        try {
            if (authentication == null || !(authentication.getPrincipal() instanceof Usuario)) {
                return "redirect:/login";
            }

            Usuario usuario = (Usuario) authentication.getPrincipal();
            if (!(usuario instanceof Estudiante)) {
                return "redirect:/login";
            }

            Estudiante estudiante = (Estudiante) usuario;

            ProyectoId proyectoId = new ProyectoId(proyectoTitulo, proyectoCuit);
            PlanDeTrabajoId planDeTrabajoId = new PlanDeTrabajoId(planNumero, proyectoId);
            ActividadId actividadId = new ActividadId(actividadNumero, planDeTrabajoId);

            Optional<Actividad> actividadOpt = actividadService.obtenerActividadPorId(actividadId);
            if (!actividadOpt.isPresent()) {
                return "redirect:/estudiante/dashboard?error=actividad_no_encontrada";
            }

            Actividad actividad = actividadOpt.get();
            if (!actividad.getPlanDeTrabajo().getProyecto().equals(estudiante.getProyecto())) {
                return "redirect:/estudiante/dashboard?error=proyecto_no_coincide";
            }

            Informe informe = informeService.crearInforme(numero, titulo, ruta, estudiante, actividad);
            return "redirect:/estudiante/dashboard?success=informe_creado";
        } catch (Exception e) {
            logger.error("Error al crear informe: {}", e.getMessage());
            return "redirect:/estudiante/dashboard?error=error_crear_informe";
        }
    }

    @GetMapping("/sin-pps")
    public String sinPPS(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            Usuario usuario = userDetails.getUsuario();

            if (usuario instanceof Estudiante) {
                Estudiante estudiante = (Estudiante) usuario;
                model.addAttribute("estudiante", estudiante);
                return "indexAlumnoSinPPS";
            }
        }
        return "redirect:/login";
    }

    @GetMapping("/entregas/hechas")
    public String mostrarEntregasHechas(Model model, Authentication authentication) {
        try {
            Optional<Usuario> usuarioOpt = usuarioService.buscarPorEmail(authentication.getName());
            if (usuarioOpt.isPresent() && usuarioOpt.get() instanceof Estudiante) {
                Estudiante estudiante = (Estudiante) usuarioOpt.get();
                
                Proyecto proyecto = estudiante.getProyecto();
                if (proyecto != null) {
                    List<Entrega> entregas = entregaService.buscarPorProyecto(proyecto);
                     model.addAttribute("entregas", entregas != null ? entregas : Collections.emptyList());
                } else {
                    logger.warn("Estudiante {} {} no tiene proyecto asignado para mostrar entregas hechas.", 
                                estudiante.getNombre(), estudiante.getApellido());
                    model.addAttribute("entregas", Collections.emptyList());
                }
               
                return "entregasHechas";
            }
            logger.warn("Usuario no autenticado o no es Estudiante en mostrarEntregasHechas");
            return "redirect:/login";
        } catch (Exception e) {
             String username = (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails)
                              ? ((CustomUserDetails) authentication.getPrincipal()).getUsername() : "Desconocido";
            logger.error("Error al mostrar entregas hechas para usuario {}: {}",
                        username, e.getMessage(), e);
            return "redirect:/error";
        }
    }

    @GetMapping("/entregas")
    public String mostrarEntregas(Model model, Authentication authentication) {
        try {
            Optional<Usuario> usuarioOpt = usuarioService.buscarPorEmail(authentication.getName());
            if (usuarioOpt.isPresent() && usuarioOpt.get() instanceof Estudiante) {
                Estudiante estudiante = (Estudiante) usuarioOpt.get();
                
                Proyecto proyecto = estudiante.getProyecto();
                 if (proyecto != null) {
                    List<Entrega> entregas = entregaService.buscarPorProyecto(proyecto);
                     model.addAttribute("entregas", entregas != null ? entregas : Collections.emptyList());
                } else {
                     logger.warn("Estudiante {} {} no tiene proyecto asignado para mostrar entregas.", 
                                estudiante.getNombre(), estudiante.getApellido());
                     model.addAttribute("entregas", Collections.emptyList());
                }

                return "entregas";
            }
            logger.warn("Usuario no autenticado o no es Estudiante en mostrarEntregas");
            return "redirect:/login";
        } catch (Exception e) {
             String username = (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails)
                              ? ((CustomUserDetails) authentication.getPrincipal()).getUsername() : "Desconocido";
            logger.error("Error al mostrar entregas para usuario {}: {}",
                        username, e.getMessage(), e);
            return "redirect:/error";
        }
    }

    @PostMapping("/subirEntrega")
    @ResponseBody
    @Transactional
    public String subirEntrega(@RequestParam("titulo") String titulo,
                              @RequestParam("descripcion") String descripcion,
                              @RequestParam("archivo") MultipartFile archivo,
                              @RequestParam("actividadId") int actividadId,
                              Authentication authentication) {
        try {
            logger.info("Iniciando proceso de subida de entrega para actividad ID: {}", actividadId);
            
            // Obtener el estudiante actual
            Optional<Estudiante> estudianteOpt = estudianteService.buscarPorEmail(authentication.getName());
            if (estudianteOpt.isEmpty()) {
                logger.error("No se encontró el estudiante con email: {}", authentication.getName());
                return "error: No se encontró el estudiante";
            }

            Estudiante estudiante = estudianteOpt.get();
            logger.info("Estudiante encontrado por email en subirEntrega: {} {}, ID: {}", estudiante.getNombre(), estudiante.getApellido(), estudiante.getId());

            // Verificar si el estudiante tiene un proyecto asignado
            Proyecto proyecto = estudiante.getProyecto();
            if (proyecto == null) {
                logger.error("El estudiante {} (ID: {}) no tiene un proyecto asignado DESPUÉS DE RECUPERARLO DEL SERVICIO.", estudiante.getNombre(), estudiante.getId());
                List<Proyecto> proyectosAlternativos = estudianteService.obtenerProyectosPorEstudiante(estudiante);
                if (proyectosAlternativos != null && !proyectosAlternativos.isEmpty()) {
                    logger.warn("Sin embargo, se encontraron {} proyectos asociados a este estudiante a través de obtenerProyectosPorEstudiante. El primero es: {}", proyectosAlternativos.size(), proyectosAlternativos.get(0).getProyectoId().titulo());
                } else {
                    logger.warn("Y tampoco se encontraron proyectos asociados a este estudiante a través de obtenerProyectosPorEstudiante.");
                }
                return "error: No tienes un proyecto asignado";
            }

            logger.info("Proyecto asignado al estudiante en subirEntrega: {}", proyecto.getProyectoId().titulo());

            // Obtener el plan de trabajo del proyecto
            PlanDeTrabajo planDeTrabajo = proyecto.getPlanDeTrabajo();
            if (planDeTrabajo == null) {
                logger.error("No se encontró el plan de trabajo para el proyecto: {}", proyecto.getProyectoId().titulo());
                return "error: No se encontró el plan de trabajo del proyecto";
            }

            logger.info("Plan de trabajo encontrado con ID: {}", planDeTrabajo.getPlanDeTrabajoId());

            // Obtener la actividad
            ActividadId actividadIdObj = new ActividadId(
                actividadId,
                planDeTrabajo.getPlanDeTrabajoId()
            );
            Optional<Actividad> actividadOpt = actividadService.obtenerActividadPorId(actividadIdObj);
            
            if (actividadOpt.isEmpty()) {
                logger.error("No se encontró la actividad con ID: {}", actividadId);
                return "error: No se encontró la actividad";
            }

            Actividad actividad = actividadOpt.get();
            logger.info("Actividad encontrada: {}", actividad.getNombre());

            // Verificar que la actividad pertenece al proyecto del estudiante
            if (!actividad.getPlanDeTrabajo().getProyecto().getProyectoId().equals(proyecto.getProyectoId())) {
                logger.error("La actividad {} no pertenece al proyecto del estudiante", actividad.getNombre());
                return "error: La actividad no pertenece a tu proyecto";
            }

            // Guardar el archivo
            String subDirectory = proyecto.getProyectoId().titulo() + "/entregas";
            String rutaArchivo = fileStorageService.storeFile(archivo, subDirectory);
            if (rutaArchivo == null) {
                logger.error("Error al guardar el archivo para la actividad: {}", actividad.getNombre());
                return "error: Error al guardar el archivo";
            }

            logger.info("Archivo guardado en: {}", rutaArchivo);

            // Crear la entrega
            Entrega entrega = new Entrega();
            entrega.setTitulo(titulo);
            entrega.setDescripcion(descripcion);
            entrega.setRutaArchivo(rutaArchivo);
            entrega.setFechaEntrega(LocalDate.now());
            entrega.setEstado(Entrega.EstadoEntrega.ENTREGADO);
            entrega.setActividad(actividad);
            entrega.setFechaLimite(actividad.getFechaLimite());

            // Guardar la entrega
            entregaService.crearEntrega(entrega);
            logger.info("Entrega creada exitosamente para la actividad: {}", actividad.getNombre());

            return "success";

        } catch (Exception e) {
            logger.error("Error al subir la entrega: " + e.getMessage(), e);
            return "error: " + e.getMessage();
        }
    }

    @GetMapping("/descargarEntrega/{id}")
    public ResponseEntity<Resource> descargarEntrega(@PathVariable int id) {
        try {
            Entrega entrega = entregaService.findById((long) id);
            if (entrega == null || entrega.getRutaArchivo() == null) {
                return ResponseEntity.notFound().build();
            }

            Path filePath = Paths.get(uploadPath, entrega.getRutaArchivo());
            Resource resource = new FileSystemResource(filePath.toFile());

            if (!resource.exists()) {
                return ResponseEntity.notFound().build();
            }

            String contentType = Files.probeContentType(filePath);
            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (Exception e) {
            logger.error("Error al descargar el archivo de la entrega {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/estudiante-sin-pps/proyecto/{cuit}/{titulo}")
    public String verDetalleProyecto(@PathVariable Long cuit,
                                   @PathVariable String titulo,
                                   Model model) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()) {
                Object principal = authentication.getPrincipal();

                if (principal instanceof CustomUserDetails) {
                    CustomUserDetails userDetails = (CustomUserDetails) principal;
                    Usuario usuario = userDetails.getUsuario();

                    if (usuario instanceof Estudiante) {
                        Estudiante estudiante = (Estudiante) usuario;
                        model.addAttribute("estudiante", estudiante);

                        Proyecto proyecto = proyectoService.getProyectoByTituloAndCuit(titulo, cuit);
                        if (proyecto != null) {
                            model.addAttribute("proyecto", proyecto);
                            return "detalleProyecto";
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Error al ver detalle del proyecto: {}", e.getMessage(), e);
        }
        return "redirect:/estudiante-sin-pps/dashboard";
    }

    @PostMapping("/estudiante-sin-pps/proyecto/{cuit}/{titulo}/inscribirse")
    public String inscribirseEnProyecto(@PathVariable Long cuit,
                                      @PathVariable String titulo,
                                      RedirectAttributes redirectAttributes) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()) {
                Object principal = authentication.getPrincipal();

                if (principal instanceof CustomUserDetails) {
                    CustomUserDetails userDetails = (CustomUserDetails) principal;
                    Usuario usuario = userDetails.getUsuario();

                    if (usuario instanceof Estudiante) {
                        Estudiante estudiante = (Estudiante) usuario;
                        proyectoService.inscribirEstudianteEnProyecto(titulo, cuit, estudiante);
                        redirectAttributes.addFlashAttribute("mensaje", "Te has inscrito exitosamente al proyecto");
                        return "redirect:/estudiante-sin-pps/dashboard";
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Error al inscribirse en el proyecto: {}", e.getMessage(), e);
            redirectAttributes.addFlashAttribute("error", "Error al inscribirse en el proyecto: " + e.getMessage());
        }
        return "redirect:/estudiante-sin-pps/dashboard";
    }

    @PostMapping("/estudiante-sin-pps/solicitud/{id}/cancelar")
    public String cancelarSolicitud(@PathVariable Long id,
                                  RedirectAttributes redirectAttributes) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()) {
                Object principal = authentication.getPrincipal();

                if (principal instanceof CustomUserDetails) {
                    CustomUserDetails userDetails = (CustomUserDetails) principal;
                    Usuario usuario = userDetails.getUsuario();

                    if (usuario instanceof Estudiante) {
                        Estudiante estudiante = (Estudiante) usuario;
                        proyectoService.cancelarSolicitud(id, estudiante);
                        redirectAttributes.addFlashAttribute("mensaje", "Solicitud cancelada exitosamente");
                        return "redirect:/estudiante-sin-pps/dashboard";
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Error al cancelar la solicitud: {}", e.getMessage(), e);
            redirectAttributes.addFlashAttribute("error", "Error al cancelar la solicitud: " + e.getMessage());
        }
        return "redirect:/estudiante-sin-pps/dashboard";
    }

    @GetMapping("/descargarActividad/{numero}/{planNumero}/{proyectoTitulo}/{proyectoCuit}")
    public ResponseEntity<Resource> descargarActividad(
            @PathVariable int numero,
            @PathVariable int planNumero,
            @PathVariable String proyectoTitulo,
            @PathVariable Long proyectoCuit) {
        try {
            ProyectoId proyectoId = new ProyectoId(proyectoTitulo, proyectoCuit);
            PlanDeTrabajoId planDeTrabajoId = new PlanDeTrabajoId(planNumero, proyectoId);
            ActividadId actividadId = new ActividadId(numero, planDeTrabajoId);

            Optional<Actividad> actividadOpt = actividadService.obtenerActividadPorId(actividadId);
            if (!actividadOpt.isPresent() || actividadOpt.get().getRutaArchivo() == null) {
                return ResponseEntity.notFound().build();
            }

            Actividad actividad = actividadOpt.get();
            Path filePath = Paths.get(actividad.getRutaArchivo());

            if (!Files.exists(filePath) || !Files.isReadable(filePath)) {
                logger.warn("Archivo no encontrado o no legible en la ruta: {}. Actividad Numero: {}, Plan: {}, Proyecto: {}, Cuit: {}", filePath, numero, planNumero, proyectoTitulo, proyectoCuit);
                return ResponseEntity.notFound().build();
            }

            Resource resource = new FileSystemResource(filePath.toFile());
            String contentType = Files.probeContentType(filePath);
            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            String filename = filePath.getFileName().toString();

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                    .body(resource);
        } catch (Exception e) {
            logger.error("Error al descargar archivo de actividad para Numero: {}, Plan: {}, Proyecto: {}, Cuit: {}. Error: {}", numero, planNumero, proyectoTitulo, proyectoCuit, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
