package ing.gpps.controller;


import ing.gpps.entity.users.Admin;
import ing.gpps.entity.users.Usuario;
import ing.gpps.security.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    public AdminController() {

    }

    // Modificar el método dashboard para asegurar que se están cargando correctamente los datos
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            // Verificar si la autenticación es nula o no está autenticada
            if (authentication == null || !authentication.isAuthenticated()) {
                throw new RuntimeException("No hay autenticación o el usuario no está autenticado");
            }
            Object principal = authentication.getPrincipal();

            if (!(principal instanceof CustomUserDetails)) {
                throw new RuntimeException("El principal no es un CustomUserDetails: " + principal.getClass().getName());
            }
            // Obtener el usuario desde CustomUserDetails
            CustomUserDetails userDetails = (CustomUserDetails) principal;
            Usuario usuario = userDetails.getUsuario();

            if (!(usuario instanceof Admin)) {
                throw new RuntimeException("El usuario no es un estudiante: " + usuario.getClass().getName());
            }
            Admin admin = (Admin) usuario;
            model.addAttribute("admin", admin);

            return "indexAdmin";
        } catch (Exception e) {
            System.err.println("Error en dashboard: " + e.getMessage());
            e.printStackTrace();
        }

        return "redirect:/login";
    }
}
