package ing.gpps.security;

import ing.gpps.entity.users.Usuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {

    private final Usuario usuario;
    private final Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(Usuario usuario) {
        this.usuario = usuario;
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        String rol = usuario.getRol();
        if (rol != null) {
            // Asegurarse de que el rol tenga el prefijo ROLE_
            if (!rol.startsWith("ROLE_")) {
                rol = "ROLE_" + rol;
            }
            authorities.add(new SimpleGrantedAuthority(rol));
        } else {
            // Si no hay rol, asignar un rol por defecto
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        }
        this.authorities = authorities;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return usuario != null ? usuario.getPassword() : null;
    }

    @Override
    public String getUsername() {
        return usuario != null ? usuario.getEmail() : null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return usuario != null;
    }
}
