package ing.gpps.security;

import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import ing.gpps.entity.users.Usuario;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        // Verificar si el principal es un CustomUserDetails
        if (authentication.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            Usuario usuario = userDetails.getUsuario();

            // Redirigir según el rol del usuario
            if ("ESTUDIANTE".equals(usuario.getRol())) {
                response.sendRedirect("/estudiante/dashboard");
            } else if ("DOCENTE_SUPERVISOR".equals(usuario.getRol())) {
                response.sendRedirect("/indexTutor");
            } else if ("TUTOR_EXTERNO".equals(usuario.getRol())) {
                response.sendRedirect("/indexEntidad");
            } else if ("ADMIN".equals(usuario.getRol())) {
                response.sendRedirect("/indexAdmin");
            } else if ("ADMIN_ENTIDAD".equals(usuario.getRol())) {
                response.sendRedirect("/admin-entidad/dashboard");
            } else {
                response.sendRedirect("/login");
            }
        } else {
            // Fallback al comportamiento anterior basado en autoridades
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

            if (hasRole(authorities, "ESTUDIANTE")) {
                response.sendRedirect("/estudiante/dashboard");
            } else if (hasRole(authorities, "DOCENTE_SUPERVISOR")) {
                response.sendRedirect("/indexTutor");
            } else if (hasRole(authorities, "TUTOR_EXTERNO")) {
                response.sendRedirect("/indexEntidad");
            } else if (hasRole(authorities, "ADMIN")) {
                response.sendRedirect("/indexAdmin");
            } else {
                response.sendRedirect("/login");
            }
        }
    }

    private boolean hasRole(Collection<? extends GrantedAuthority> authorities, String role) {
        return authorities.stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_" + role));
    }
}
