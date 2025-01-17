package com.pp1.salve.api.usuario;


import org.springframework.web.bind.annotation.CrossOrigin;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.pp1.salve.model.usuario.Usuario;
import com.pp1.salve.model.usuario.UsuarioRepository;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/usuario")
@CrossOrigin
@Tag(name = "Usuario", description = "API de Usuario")
public class UsuarioController {

     @Autowired
    private UsuarioRepository service;

    @GetMapping
    public List<Usuario> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Usuario>> getById(@PathVariable String id) {
        return ResponseEntity.ok(service.findById(id));
    }

  
}
