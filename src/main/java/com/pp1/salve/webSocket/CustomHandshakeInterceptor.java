package com.pp1.salve.webSocket;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import com.pp1.salve.configs.interceptor.UserMapper;
import com.pp1.salve.model.usuario.Usuario;

import lombok.extern.slf4j.Slf4j;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.jwt.JwtException;
@Slf4j
public class CustomHandshakeInterceptor implements HandshakeInterceptor {
    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String issuerUri;
    @Autowired
    private UserMapper userMapper;
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) throws Exception {
        String query = request.getURI().getQuery();
        String token = query != null ? query.split("=")[1] : null;
        if (token != null) {
            JwtDecoder jwtDecoder = JwtDecoders.fromIssuerLocation(issuerUri);
            try {
                Jwt jwt = jwtDecoder.decode(token);
                Usuario username = userMapper.toUsuario(jwt.getClaims());
                attributes.put("user", username);
            } catch (JwtException e) {
                log.error("Erro, issuer tem url:", issuerUri);
                throw e;
                //response.setStatusCode(HttpStatus.UNAUTHORIZED);
                //return false;
            }
        } else {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return false;
        }
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
                               Exception exception) {
        // Nada a fazer aqui
    }
}