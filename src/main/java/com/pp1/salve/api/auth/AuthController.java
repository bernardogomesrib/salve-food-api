package com.pp1.salve.api.auth;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pp1.salve.kc.KeycloakService;

import io.swagger.v3.oas.annotations.tags.Tag;



@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Authentication points")
public class AuthController {
    @Autowired
    private KeycloakService keycloakService;

    @PostMapping("create")
    public ResponseEntity<?> postMethodName(@RequestBody AccountCreationRequest entity) {
        Map<String, String> map = new java.util.HashMap<>();
        if (keycloakService.createAccount(entity.getFirstName(), entity.getLastName(), entity.getEmail(), entity.getPassword(),entity.getEmail())) {
            map.put("message", "User created successfully");
            return ResponseEntity.status(201).body(map);
        } else {
            map.put("message", "Error creating user");
            return ResponseEntity.status(500).body(map);
        }
    }
    @PostMapping("login")
    public ResponseEntity<?> postLogin(@RequestBody AuthRequest loginRequest) {
        try {
            return ResponseEntity.ok(keycloakService.login(loginRequest.getUsername(), loginRequest.getPassword()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
    @PostMapping("refresh")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshRequest refreshtoken) {
        return ResponseEntity.ok().body(keycloakService.refresh(refreshtoken.getRefreshToken()));
    }
    
    
}
