package com.pp1.salve.api.entregador;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

import com.pp1.salve.model.entregador.Entregador;
import com.pp1.salve.model.entregador.EntregadorService;
import com.pp1.salve.model.loja.Loja;
import com.pp1.salve.model.loja.LojaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping("/api/entregador")
@CrossOrigin
@Tag(name = "Entregador", description = "pontos de interação com um Entregador")
public class EntregadorController {

    @Autowired
    private EntregadorService service;

    @Autowired
    private LojaService lojaService;

    @PreAuthorize("hasRole('dono_de_loja')")
    @GetMapping("/{lojaId}")
    public ResponseEntity<List<Entregador>> findByLoja(@PathVariable Long lojaId) {
        try {
            List<Entregador> entregadores = service.findByLoja(lojaId);
            return ResponseEntity.ok(entregadores);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PreAuthorize("hasRole('dono_de_loja')")
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<Entregador> save(
            @ModelAttribute @Valid EntregadorRequest request,
            Authentication authentication) throws Exception {

        Loja loja = lojaService.findById(request.getLojaId());
        if (loja == null) {
            throw new Exception("Loja não encontrada com o ID fornecido: " + request.getLojaId());
        }

        Entregador entregador = request.build(loja);

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

    @PreAuthorize("hasRole('dono_de_loja')")
    @Operation(summary = "Busca entregador por id")
    @GetMapping("/unico/{id}")
    public Entregador getById(@RequestParam Long id) throws Exception {
        return service.findById(id);
    }

    @PreAuthorize("hasRole('dono_de_loja')")
    @GetMapping("meus")
    public ResponseEntity<List<Entregador>> getMeusEntregadores(Authentication authentication) throws Exception {
        return ResponseEntity.ok().body(service.findMeusEntregadoresDisponiveis(authentication));
    }

    @Operation(summary = "Atualiza entregador", description = "Atualiza entregador, se quizer alterar a imagem, envie o arquivo, caso não, pode deixar de boas, se quiser editar o status também mande o status, caso não é de boas também, irá ser ignorado o status")
    @PreAuthorize("hasRole('dono_de_loja')")
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Entregador> putEntregador(@PathVariable Long id, @Valid @ModelAttribute EntregadorRequest entregador, Authentication authentication) throws Exception {
        
        return ResponseEntity.ok(service.atualizarEntregador(id, entregador, authentication));
    }

}