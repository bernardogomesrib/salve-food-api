package com.pp1.salve.api.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
                entity.getPassword(), entity.getEmail());
    }

    @PostMapping("login")
    public ResponseEntity<?> postLogin(@RequestBody @Valid AuthRequest loginRequest) {
        return keycloakService.login(loginRequest.getUsername(), loginRequest.getPassword());
    }

    @PostMapping("refresh")
    public ResponseEntity<?> refreshToken(@RequestBody @Valid RefreshRequest refreshtoken) {
        return keycloakService.refresh(refreshtoken.getRefreshToken());
    }

    @PostMapping("logout")
    public ResponseEntity<?> log(@RequestBody @Valid RefreshRequest refreshtoken) {
        return keycloakService.logout(refreshtoken.getRefreshToken());
    }

}
