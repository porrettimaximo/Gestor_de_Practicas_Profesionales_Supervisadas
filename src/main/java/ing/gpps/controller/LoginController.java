package ing.gpps.controller;


import ing.gpps.security.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String showLoginPage() {
        // Si el usuario ya está autenticado, redirigir según su rol
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !auth.getName().equals("anonymousUser")) {
            if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ESTUDIANTE"))) {
                return "redirect:/indexAlumno";
            } else if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_TUTOR"))) {
                return "redirect:/indexTutor";
            } else if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ENTIDAD"))) {
                return "redirect:/indexEntidad";
            } else if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
                return "redirect:/indexAdmin";
            }
        }
        return "login";
    }

    @GetMapping("/indexAlumno")
    public String showIndexAlumno() {
        // Verificar si el usuario está autenticado
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !auth.getName().equals("anonymousUser")) {
            // Redirigir al dashboard del estudiante
            return "redirect:/estudiante/dashboard";
        }
        return "redirect:/login";
    }

    @GetMapping("/indexAlumnoSinPPS")
    public String showIndexAlumnoSinPPS() {
        // Redirigir a la vista sin PPS
        return "redirect:/estudiante/sin-pps";
    }

    @GetMapping("/indexAdmin")
    public String showIndexAdminPage() {
        // Verificar si el usuario está autenticado
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !auth.getName().equals("anonymousUser")) {
            // Redirigir al dashboard del estudiante
            return "redirect:/admin/dashboard";
        }
        return "redirect:/login";
    }

    @GetMapping("/indexTutor")
    public String showIndexTutorPage() {
        // Verificar si el usuario está autenticado
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !auth.getName().equals("anonymousUser")) {
            // Redirigir al dashboard del tutor
            return "redirect:/docente_supervisor/dashboard";
        }
        return "redirect:/login";
    }

    @GetMapping("/indexEntidad")
    public String showIndexEntidad() {
        return "indexEntidad";
    }

    @GetMapping("/")
    public String home() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !auth.getName().equals("anonymousUser")) {
            CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
            String rol = userDetails.getUsuario().getRol();

            if ("ESTUDIANTE".equals(rol)) {
                return "redirect:/estudiante/dashboard";
            } else if ("DOCENTE_SUPERVISOR".equals(rol)) {
                return "redirect:/docente_supervisor";
            } else if ("TUTOR_EXTERNO".equals(rol)) {
                return "redirect:/indexEntidad";
            } else if ("ADMIN".equals(rol)) {
                return "redirect:/admin/dashboard";
            }
        }
        return "redirect:/login";
    }
}
