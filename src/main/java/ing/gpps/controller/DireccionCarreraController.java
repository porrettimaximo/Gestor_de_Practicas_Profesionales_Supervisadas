package ing.gpps.controller;

import ing.gpps.entity.institucional.Entidad;
import ing.gpps.entity.institucional.Proyecto;
import ing.gpps.entity.users.Admin;
import ing.gpps.entity.users.DireccionDeCarrera;
import ing.gpps.entity.users.Usuario;
import ing.gpps.security.CustomUserDetails;
import ing.gpps.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/direccion")
public class DireccionCarreraController {

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private EntidadService entidadService;
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private ProyectoService proyectoService;
    @Autowired
    private AdminEntidadService adminEntidadService;
    @Autowired
    private EstudianteService estudianteService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            // Verificar autenticación y usuario
            if (authentication == null || !authentication.isAuthenticated()) {
                logger.warn("Intento de acceso no autorizado al dashboard");
                return "redirect:/login";
            }

            Object principal = authentication.getPrincipal();
            if (!(principal instanceof CustomUserDetails)) {
                logger.warn("Usuario no es CustomUserDetails");
                return "redirect:/login";
            }

            CustomUserDetails userDetails = (CustomUserDetails) principal;
            Usuario usuario = userDetails.getUsuario();

            if (!(usuario instanceof DireccionDeCarrera)) {
                logger.warn("Usuario no es DireccionDeCarrera");
                return "redirect:/login";
            }

            DireccionDeCarrera direccionDeCarrera = (DireccionDeCarrera) usuario;
            if (direccionDeCarrera == null || direccionDeCarrera.getId() == null) {
                logger.error("Error: Usuario no encontrado o ID nulo");
                return "redirect:/error";
            }

            model.addAttribute("direccion", direccionDeCarrera);

            List<Proyecto> proyectos = proyectoService.obtenerTodos();
            model.addAttribute("proyectos", proyectos);

            return "indexDireccionDeCarrera";
        } catch (Exception e) {
            logger.error("Error en dashboard: {}", e.getMessage());
            return "redirect:/error";
        }
    }
} 