package com.pp1.salve.api.endereco;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pp1.salve.model.endereco.Endereco;
import com.pp1.salve.model.endereco.EnderecoService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController

@RequestMapping("/api/endereco")
@Tag(name = "Endereço", description = "API de Endereço")
@CrossOrigin
public class EnderecoController {

    @Autowired
    private EnderecoService service;
    @PreAuthorize("hasRole('usuario')")
    @GetMapping
    public List<Endereco> getAll() {
        return service.findAll();
    }
    @PreAuthorize("hasRole('usuario')")
    @GetMapping("/{id}")
    public ResponseEntity<Endereco> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }
    @PreAuthorize("hasRole('usuario')")
    @PostMapping
    public ResponseEntity<Endereco> create(@RequestBody EnderecoRequest endereco,@AuthenticationPrincipal Jwt jwt) {
        Endereco end = endereco.build();
        end.setUsuario(jwt.getClaim("sub"));
        return ResponseEntity.ok(service.save(end));
    }
    @PreAuthorize("hasRole('usuario')")
    @PutMapping("/{id}")
    public ResponseEntity<Endereco> update(@PathVariable Long id, @RequestBody Endereco endereco) {
        endereco.setId(id);
        return ResponseEntity.ok(service.save(endereco));
    }
    @PreAuthorize("hasRole('usuario')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
