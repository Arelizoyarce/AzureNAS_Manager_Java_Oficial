package com.azurenas.azure_nas_manager;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UsuarioRepository usuarioRepository;
    public CustomUserDetailsService(UsuarioRepository usuarioRepository) { this.usuarioRepository = usuarioRepository; }
    @Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        Usuario u = usuarioRepository.findByCorreo(correo).orElseThrow(() -> new UsernameNotFoundException("No encontrado"));
        String rol = (u.getRol() != null) ? u.getRol().getNombre() : "USUARIO";
        return User.builder().username(u.getCorreo()).password(u.getPassword_hash()).roles(rol).build();
    }
}