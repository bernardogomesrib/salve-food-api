package com.pp1.salve.model.usuario;


import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.pp1.salve.exceptions.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    
    
    public Usuario findUsuario(Authentication authentication) {
        return usuarioRepository.findById(authentication.getName()).orElseThrow(() -> new ResourceNotFoundException("Usuario não encontrado"));
    }

    public Usuario findUsuarioByEmail(String email) {
        return usuarioRepository.findByEmail(email).orElse(null);
    }
}
