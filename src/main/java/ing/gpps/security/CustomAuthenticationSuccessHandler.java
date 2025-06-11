package ing.gpps.security;

import ing.gpps.entity.users.Admin;
import ing.gpps.entity.users.DireccionDeCarrera;
import ing.gpps.entity.users.DocenteSupervisor;
import ing.gpps.entity.users.Estudiante;
import ing.gpps.entity.users.TutorExterno;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationSuccessHandler.class);

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                      Authentication authentication) throws IOException, ServletException {
        try {
            if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
                CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
                String targetUrl = determineTargetUrl(userDetails);
                logger.info("Redirigiendo usuario {} a {}", userDetails.getUsername(), targetUrl);
                getRedirectStrategy().sendRedirect(request, response, targetUrl);
            } else {
                logger.warn("Autenticación exitosa pero sin detalles de usuario válidos");
                super.onAuthenticationSuccess(request, response, authentication);
            }
        } catch (Exception e) {
            logger.error("Error al manejar autenticación exitosa: {}", e.getMessage());
            super.onAuthenticationSuccess(request, response, authentication);
        }
    }

    private String determineTargetUrl(CustomUserDetails userDetails) {
        if (userDetails.getUsuario() instanceof Estudiante) {
            Estudiante estudiante = (Estudiante) userDetails.getUsuario();
            if (estudiante.getProyecto() == null) {
                return "/estudiante-sin-pps/dashboard";
            }
            return "/estudiante/dashboard";
        } else if (userDetails.getUsuario() instanceof DocenteSupervisor) {
            return "/docente-supervisor/dashboard";
        } else if (userDetails.getUsuario() instanceof TutorExterno) {
            return "/tutor-externo/dashboard";
        } else if (userDetails.getUsuario() instanceof Admin) {
            return "/admin/dashboard";
        } else if (userDetails.getUsuario() instanceof DireccionDeCarrera) {
            return "/direccion/dashboard";
        }
        return "/login";
    }
}
