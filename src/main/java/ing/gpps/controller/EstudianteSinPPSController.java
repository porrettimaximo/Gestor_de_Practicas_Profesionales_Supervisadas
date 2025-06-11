package ing.gpps.controller;

import ing.gpps.entity.institucional.Proyecto;
import ing.gpps.entity.users.Estudiante;
import ing.gpps.entity.users.Usuario;
import ing.gpps.security.CustomUserDetails;
import ing.gpps.service.ProyectoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/estudiante-sin-pps")
public class EstudianteSinPPSController {
    private static final Logger logger = LoggerFactory.getLogger(EstudianteSinPPSController.class);

    @Autowired
    private ProyectoService proyectoService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
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

                        List<Proyecto> proyectosPostulados = proyectoService.obtenerProyectosPostuladosPorEstudiante(estudiante);
                        List<Proyecto> proyectosDisponibles = proyectoService.obtenerProyectosDisponibles(estudiante);
                        Map<Proyecto, Integer> cantidadPostulantes = proyectoService.obtenerCantidadPostulantesPorProyecto(proyectosPostulados);

                        model.addAttribute("proyectosPostulados", proyectosPostulados);
                        model.addAttribute("proyectosDisponibles", proyectosDisponibles);
                        model.addAttribute("cantidadPostulantes", cantidadPostulantes);

                        return "indexAlumnoSinPPS";
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Error en dashboard para estudiante sin PPS: {}", e.getMessage(), e);
        }
        return "redirect:/login";
    }

    @GetMapping("/proyecto/{cuit}/{titulo}")
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

    @PostMapping("/proyecto/{cuit}/{titulo}/inscribirse")
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
            redirectAttributes.addFlashAttribute("error", "Error al inscribirse en el proyecto");
        }
        return "redirect:/estudiante-sin-pps/dashboard";
    }

    @PostMapping("/solicitud/{id}/cancelar")
    public String cancelarSolicitud(@PathVariable Long id, RedirectAttributes redirectAttributes) {
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
            logger.error("Error al cancelar solicitud: {}", e.getMessage(), e);
            redirectAttributes.addFlashAttribute("error", "Error al cancelar la solicitud");
        }
        return "redirect:/estudiante-sin-pps/dashboard";
    }
} 