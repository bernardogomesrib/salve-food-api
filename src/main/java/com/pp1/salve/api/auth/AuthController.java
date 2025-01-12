package com.pp1.salve.api.auth;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.oauth2.jwt.Jwt;
import com.pp1.salve.kc.KeycloakService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Authentication points")
public class AuthController {
    @Autowired
    private KeycloakService keycloakService;

    @PostMapping("create")
    public ResponseEntity<?> postMethodName(@RequestBody @Valid AccountCreationRequest entity) {
        return keycloakService.createAccount(entity.getFirstName(), entity.getLastName(), entity.getEmail(),
                entity.getPassword(),entity.getPhoneNumber());
    }

    @PostMapping("login")
    public ResponseEntity<?> postLogin(@RequestBody @Valid AuthRequest loginRequest) {
        return keycloakService.login(loginRequest.getUsername(), loginRequest.getPassword());
    }

    @PreAuthorize("hasRole('usuario')")
    @PostMapping("refresh")
    public ResponseEntity<?> refreshToken(@RequestBody @Valid RefreshRequest refreshtoken) {
        return keycloakService.refresh(refreshtoken.getRefreshToken());
    }
    @PreAuthorize("hasRole('usuario')")
    @PostMapping("logout")
    public ResponseEntity<?> log(@AuthenticationPrincipal Jwt jwt, @RequestBody @Valid RefreshRequest refreshtoken) {
        return keycloakService.logout(refreshtoken.getRefreshToken());
    }

    @PreAuthorize("hasRole('usuario')")
    @GetMapping("introspect")
    public ResponseEntity<Map<String, Object>> getUserInfo(@AuthenticationPrincipal Jwt jwt) {
        Map<String, Object> claims = jwt.getClaims();
        return ResponseEntity.ok(claims);
    }
    @PreAuthorize("hasRole('usuario')")
    @PostMapping("logistaAddRole")
    public ResponseEntity<?> postMethodName(@AuthenticationPrincipal Jwt jwt) {
        Map<String,Object> usuario = getUserInfo(jwt).getBody();
        return keycloakService.addRoleToUser(usuario.get("sub").toString(),"dono_de_loja");
    }
    @PreAuthorize("hasRole('usuario')")
    @GetMapping("roles")
    public ResponseEntity<?> getAllRoles() {
        return keycloakService.listAllRoles();
    }
    
    
}
