package com.pp1.salve.api.endereco;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pp1.salve.api.location.LocationController;
import com.pp1.salve.model.endereco.Endereco;
import com.pp1.salve.model.endereco.EnderecoService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController

@RequestMapping("/api/endereco")
@Tag(name = "Endereço", description = "pontos para interação com Endereço")
@CrossOrigin
@RequiredArgsConstructor
public class EnderecoController {
    private final LocationController locationController;
    
    private final EnderecoService service;

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

    @SuppressWarnings("unchecked")
    @PreAuthorize("hasRole('usuario')")
    @PostMapping
    public ResponseEntity<Endereco> create(@RequestBody @Valid EnderecoRequest endereco, Authentication authentication) {
    
        Map<String, Double> coordinates = (Map<String, Double>) locationController.getCoordinates(endereco.getRua(), endereco.getNumero(), endereco.getBairro(), endereco.getCidade(), endereco.getEstado()).getBody();

        Endereco end = endereco.build();
        end.setLatitude(coordinates.get("latitude"));
        end.setLongitude(coordinates.get("longitude"));
        end.setUsuario(authentication.getName());

        return ResponseEntity.ok(service.save(end));
    }

    @PreAuthorize("hasRole('usuario')")
    @PutMapping()
    public ResponseEntity<Endereco> update(@RequestBody Endereco endereco, Authentication authentication) {
        return ResponseEntity.ok(service.update(endereco, authentication));
    }

    @PreAuthorize("hasRole('usuario')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, Authentication authentication) {
        service.deleteById(id, authentication);
        return ResponseEntity.ok().build();
    }

    @GetMapping("mine")
    public ResponseEntity<List<Endereco>> getEnderecosDoUsuario(Authentication authentication) {

        return ResponseEntity.ok().body(service.findByUsuario(authentication));
    }

}
