package com.pp1.salve.config.auditAware;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.pp1.salve.model.usuario.Usuario;


public class ApplicationAuditorAware implements AuditorAware<Usuario> {

    @Override
    public @NonNull Optional<Usuario> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("Unable to retrieve the logged-in user.");
        }
        Usuario usuario = (Usuario) authentication.getPrincipal();
        return Optional.of(usuario);
    }
}