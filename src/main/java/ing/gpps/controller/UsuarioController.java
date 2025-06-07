package ing.gpps.controller;

import ing.gpps.entity.users.Usuario;
import ing.gpps.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {
    private static final Logger logger = LoggerFactory.getLogger(UsuarioController.class);
    private final UsuarioService usuarioService;

    @Autowired
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public String crearUsuario(@ModelAttribute Usuario usuario, Model model, RedirectAttributes redirectAttributes) {
        try {
            if (usuario == null) {
                logger.warn("Intento de crear usuario con datos nulos");
                redirectAttributes.addFlashAttribute("error", "Los datos del usuario son requeridos");
                return "redirect:/login";
            }

            // Validar campos requeridos
            if (usuario.getEmail() == null || usuario.getEmail().trim().isEmpty()) {
                logger.warn("Intento de crear usuario sin email");
                redirectAttributes.addFlashAttribute("error", "El email es requerido");
                return "redirect:/login";
            }

            if (usuario.getPassword() == null || usuario.getPassword().trim().isEmpty()) {
                logger.warn("Intento de crear usuario sin contraseña");
                redirectAttributes.addFlashAttribute("error", "La contraseña es requerida");
                return "redirect:/login";
            }

            // Validar formato de email
            if (!usuario.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                logger.warn("Intento de crear usuario con email inválido: {}", usuario.getEmail());
                redirectAttributes.addFlashAttribute("error", "El formato del email no es válido");
                return "redirect:/login";
            }

            // Validar longitud mínima de contraseña
            if (usuario.getPassword().length() < 8) {
                logger.warn("Intento de crear usuario con contraseña débil");
                redirectAttributes.addFlashAttribute("error", "La contraseña debe tener al menos 8 caracteres");
                return "redirect:/login";
            }

            usuarioService.registrarUsuario(usuario);
            logger.info("Usuario creado exitosamente: {}", usuario.getEmail());
            redirectAttributes.addFlashAttribute("mensaje", "Usuario creado exitosamente");
            return "redirect:/indexConPPs";
        } catch (Exception e) {
            logger.error("Error al crear usuario: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Error al crear usuario: " + e.getMessage());
            return "redirect:/login";
        }
    }
/*
    @GetMapping("/{id}")
    public String buscarPorId(@PathVariable int id, Model model) {
        Usuario usuario = usuarioService.buscarPorId(id);
        if (usuario != null) {
            model.addAttribute("usuario", usuario);
            return "detalle-usuario";  // Muestra vista de detalle
        } else {
            model.addAttribute("error", "Usuario no encontrado");
            return "error";  // Muestra página de error
        }
    }
*/
}



