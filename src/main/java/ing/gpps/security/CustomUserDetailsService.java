package ing.gpps.security;

import ing.gpps.entity.users.Usuario;
import ing.gpps.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        logger.debug("Intentando cargar usuario con email: {}", email);

        if (!StringUtils.hasText(email)) {
            logger.error("Se intentó autenticar con un email vacío");
            throw new UsernameNotFoundException("El email no puede estar vacío");
        }

        final String emailTrimmed = email.trim();
        if (!emailTrimmed.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            logger.error("Se intentó autenticar con un email inválido: {}", emailTrimmed);
            throw new UsernameNotFoundException("Formato de email inválido");
        }

        Usuario usuario = usuarioRepository.findByEmail(emailTrimmed)
                .orElseThrow(() -> {
                    logger.error("Usuario no encontrado con email: {}", emailTrimmed);
                    return new UsernameNotFoundException("Usuario no encontrado con email: " + emailTrimmed);
                });

        logger.debug("Usuario encontrado: {} con rol: {}", usuario.getEmail(), usuario.getRol());
        return new CustomUserDetails(usuario);
    }
}
