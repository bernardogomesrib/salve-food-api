package com.pp1.salve.configs.interceptor;


import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import com.pp1.salve.exceptions.TokenSemIdException;
import com.pp1.salve.model.usuario.Usuario;


@Service
public class UserMapper {
    public Usuario toUsuario(Map<String,Object> token) {
        Usuario user = new Usuario();
        if(token.containsKey("sub")) {
            user.setId(token.get("sub").toString());
        }else{
            throw new TokenSemIdException("User id not found in token");
        }
        if(token.containsKey("given_name")) {
            user.setFirstName(token.get("given_name").toString());

        }else if(token.containsKey("nickname")) {
            user.setFirstName(token.get("nickname").toString());
        }
        if(token.containsKey("family_name")) {
            user.setLastName(token.get("family_name").toString());
        }
        user.setEmail(token.get("email").toString());

        user.setLastSeenAt(LocalDateTime.now());
        return user;
    }

    public Usuario updateUsuario(Usuario Usuario, Jwt token) {
        Usuario newUsuario = toUsuario(token.getClaims());
        newUsuario.setId(Usuario.getId());
        return newUsuario;
    }

    public UsuarioResponse toUsuarioResponse(Usuario Usuario) {
        return UsuarioResponse.builder()
            .id(Usuario.getId())
            .firstName(Usuario.getFirstName())
            .lastName(Usuario.getLastName())
            .email(Usuario.getEmail())
            .lastSeenAt(Usuario.getLastSeenAt())
            .isOnline(Usuario.isOnline())
            .build();
    }
}
