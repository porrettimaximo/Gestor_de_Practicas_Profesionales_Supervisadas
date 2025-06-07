package ing.gpps.controller;

import ing.gpps.entity.institucional.Entidad;
import ing.gpps.entity.institucional.TipoEntidad;
import ing.gpps.entity.users.Admin;
import ing.gpps.entity.users.Usuario;
import ing.gpps.security.CustomUserDetails;
import ing.gpps.service.EntidadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);
    private final EntidadService entidadService;

    @Autowired
    public AdminController(EntidadService entidadService) {
        this.entidadService = entidadService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

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

            if (!(usuario instanceof Admin)) {
                logger.warn("Usuario no es Admin");
                return "redirect:/login";
            }

            Admin admin = (Admin) usuario;
            if (admin == null || admin.getId() == null) {
                logger.error("Error: Admin no encontrado o ID nulo");
                return "redirect:/error";
            }

            model.addAttribute("admin", admin);

            List<Entidad> entidades = entidadService.obtenerTodas();
            if (entidades == null) {
                logger.error("Error al obtener entidades");
                return "redirect:/error";
            }
            model.addAttribute("entidades", entidades);

            return "indexAdmin";
        } catch (Exception e) {
            logger.error("Error en dashboard: {}", e.getMessage());
            return "redirect:/error";
        }
    }

    @PostMapping("/entidad/agregar")
    public String agregarEntidad(@RequestParam("nombre") String nombre,
                               @RequestParam("cuit") String cuit,
                               @RequestParam("tipo") String tipo,
                               @RequestParam("ubicacion") String ubicacion,
                               @RequestParam("contacto") String contacto,
                               RedirectAttributes redirectAttributes) {
        try {
            // Validar campos requeridos
            if (nombre == null || nombre.trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("mensaje", "El nombre es requerido");
                redirectAttributes.addFlashAttribute("tipoMensaje", "error");
                return "redirect:/admin/dashboard";
            }

            if (cuit == null || cuit.trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("mensaje", "El CUIT es requerido");
                redirectAttributes.addFlashAttribute("tipoMensaje", "error");
                return "redirect:/admin/dashboard";
            }

            if (tipo == null || tipo.trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("mensaje", "El tipo es requerido");
                redirectAttributes.addFlashAttribute("tipoMensaje", "error");
                return "redirect:/admin/dashboard";
            }

            if (ubicacion == null || ubicacion.trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("mensaje", "La ubicación es requerida");
                redirectAttributes.addFlashAttribute("tipoMensaje", "error");
                return "redirect:/admin/dashboard";
            }

            if (contacto == null || contacto.trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("mensaje", "El contacto es requerido");
                redirectAttributes.addFlashAttribute("tipoMensaje", "error");
                return "redirect:/admin/dashboard";
            }

            // Validar formato del CUIT
            if (!cuit.matches("\\d{11}")) {
                redirectAttributes.addFlashAttribute("mensaje", "El CUIT debe tener 11 dígitos");
                redirectAttributes.addFlashAttribute("tipoMensaje", "error");
                return "redirect:/admin/dashboard";
            }

            // Validar tipo de entidad
            TipoEntidad tipoEntidad;
            try {
                tipoEntidad = TipoEntidad.valueOf(tipo.toUpperCase());
            } catch (IllegalArgumentException e) {
                redirectAttributes.addFlashAttribute("mensaje", "Tipo de entidad inválido");
                redirectAttributes.addFlashAttribute("tipoMensaje", "error");
                return "redirect:/admin/dashboard";
            }

            // Crear y guardar la entidad
            Entidad entidad = new Entidad();
            entidad.setNombre(nombre.trim());
            entidad.setCuit(Long.parseLong(cuit));
            entidad.setTipo(tipoEntidad);
            entidad.setUbicacion(ubicacion.trim());
            entidad.setEmail(contacto.trim());

            entidadService.registrarEntidad(entidad);

            logger.info("Entidad agregada exitosamente: {} (CUIT: {})", nombre, cuit);
            redirectAttributes.addFlashAttribute("mensaje", "Entidad agregada exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (NumberFormatException e) {
            logger.error("Error al parsear CUIT: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("mensaje", "El CUIT debe ser un número válido");
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        } catch (Exception e) {
            logger.error("Error al agregar entidad: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("mensaje", "Error al agregar la entidad: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        }
        return "redirect:/admin/dashboard";
    }
}
