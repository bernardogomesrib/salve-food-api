package com.pp1.salve.model.usuario;


import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.pp1.salve.exceptions.ResourceNotFoundException;
import com.pp1.salve.minio.MinIOInterfacing;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final MinIOInterfacing minIOInterfacing;
    
    public Usuario findUsuario(Authentication authentication) {
        return usuarioRepository.findById(authentication.getName()).orElseThrow(() -> new ResourceNotFoundException("Usuario não encontrado"));
    }

    public Usuario findUsuarioByEmail(String email) {
        return usuarioRepository.findByEmail(email.toLowerCase()).orElse(null);
    }

    public PfpResponse image(Authentication authentication,@NotNull MultipartFile file) throws Exception {
        return new PfpResponse(minIOInterfacing.salvarProfilePicture(authentication.getName(), "pfp", file));
    }
    public String getImage(Authentication authentication) throws Exception {
        return minIOInterfacing.pegarImagemDePerfil(authentication.getName(), "pfp");
    }
}
