package com.pp1.salve.api.usuario;


import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.pp1.salve.api.auth.AuthController;
import com.pp1.salve.exceptions.LocationException;
import com.pp1.salve.kc.KeycloakService;
import com.pp1.salve.model.usuario.Usuario;
import com.pp1.salve.model.usuario.UsuarioRepository;
import com.pp1.salve.model.usuario.UsuarioService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/api/usuario")
@CrossOrigin
@Tag(name = "Usuario", description = "API de Usuario")
@RequiredArgsConstructor
public class UsuarioController {
    private final KeycloakService keycloakService;
    private final UsuarioRepository service;
    private final AuthController authController;
    private final UsuarioService usuarioService;
    @GetMapping
    public List<Usuario> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Usuario>> getById(@PathVariable String id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping("/virarEntregador")
    public ResponseEntity<?> postCriaRoleDeLogistaNoUsuario(@AuthenticationPrincipal Jwt jwt,Authentication authentication) throws Exception {
        Map<String, Object> usuario = authController.getUserInfo(jwt,authentication).getBody();
        return keycloakService.addRoleToUser(usuario.get("sub").toString(), "entregador");
    }

    @PostMapping(path = "pfp", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> postPfp(Authentication authentication,@RequestBody Map<String, Object> payload) throws Exception {
        try {
            @SuppressWarnings("unchecked")
            byte[] file = new byte[((List<Integer>) payload.get("file")).size()];
            int i = 0;
            for (Integer val : ((List<?>) payload.get("file")).stream().map(o -> (Integer) o).toList()) {
                file[i++] = val.byteValue();
            }
            String mimetype = (String) payload.get("mimetype");
            return ResponseEntity.ok().body(usuarioService.image(authentication, file,mimetype));
            
        } catch (Exception e) {
            throw new LocationException("Erro ao converter imagem");
        }
    }
}
