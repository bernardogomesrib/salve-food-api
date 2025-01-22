package com.pp1.salve.api.entregador;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pp1.salve.kc.KeycloakService;
import com.pp1.salve.model.entregador.Entregador;
import com.pp1.salve.model.entregador.EntregadorService;
import com.pp1.salve.model.loja.Loja;
import com.pp1.salve.model.loja.LojaService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/entregador")
@CrossOrigin
@Tag(name = "Entregador", description = "pontos de interação com um Entregador")
public class EntregadorController {

    @Autowired
    private EntregadorService service;

    @Autowired
    private KeycloakService keycloakService;

    @Autowired
    private LojaService lojaService;

    @GetMapping("/{lojaId}")
    public ResponseEntity<List<Entregador>> findByLoja(@PathVariable Long lojaId) {
        try {
            List<Entregador> entregadores = service.findByLoja(lojaId);
            return ResponseEntity.ok(entregadores);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping(consumes = "multipart/form-data")
public ResponseEntity<Entregador> save(
        @ModelAttribute @Valid EntregadorRequest request,
        Authentication authentication) throws Exception {

    ResponseEntity<?> response = keycloakService.createAccount(
            request.getFirstName(),
            request.getLastName(),
            request.getEmail(),
            request.getPassword(),
            request.getPhoneNumber()
    );

    if (!response.getStatusCode().is2xxSuccessful()) {
        throw new RuntimeException("Falha ao criar usuário no Keycloak: " + response.getStatusCode());
    }

    if (response.getHeaders().getLocation() == null) {
        throw new RuntimeException("Resposta do Keycloak não veio com Location no header.");
    }

    String locationHeader = response.getHeaders().getLocation().toString();
    String keycloakUserId = locationHeader.substring(locationHeader.lastIndexOf("/") + 1);

    Loja loja = lojaService.findById(request.getLojaId());
    if (loja == null) {
        throw new Exception("Loja não encontrada com o ID fornecido: " + request.getLojaId());
    }

    Entregador entregador = request.build(loja, keycloakUserId);

    Entregador savedEntregador = service.save(entregador, request.getFile(), authentication);

    return ResponseEntity.ok(savedEntregador);
}


    @PreAuthorize("hasRole('dono_de_loja')")
    @PatchMapping(value = "/{id}/status")
    public ResponseEntity<Entregador> updateStatus(@PathVariable Long id, @RequestParam Boolean disponivel,
            Authentication authentication) throws Exception {
        return ResponseEntity.ok(service.updateStatus(id, disponivel, authentication));
    }

    @PreAuthorize("hasRole('dono_de_loja')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, Authentication authentication) throws Exception {
        service.delete(id, authentication);
        return ResponseEntity.noContent().build();
    }
}