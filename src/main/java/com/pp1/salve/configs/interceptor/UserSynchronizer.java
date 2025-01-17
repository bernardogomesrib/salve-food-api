package com.pp1.salve.configs.interceptor;

import java.util.Optional;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import com.pp1.salve.model.usuario.Usuario;
import com.pp1.salve.model.usuario.UsuarioRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserSynchronizer {
    private final UsuarioRepository userRepository;
    private final UserMapper userMapper;
    public void synchronizeUser(Jwt token) {
        log.info("Sincronizando usuario");
        getEmail(token).ifPresent(email -> {
            Usuario user = userMapper.toUsuario(token.getClaims());
            userRepository.save(user);
        });
    }

    public Optional<String> getEmail(Jwt token) {
        return Optional.ofNullable(token.getClaim("email"));
    }


}
