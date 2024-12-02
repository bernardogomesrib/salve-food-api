package com.pp1.salve.api.pedido;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pp1.salve.model.pedido.AlteracoesPedido;
import com.pp1.salve.model.pedido.AlteracoesPedidoService;

@RestController
@RequestMapping("/alteracoes-pedido")
@CrossOrigin
public class AlteracoesPedidoController {

  @Autowired
  private AlteracoesPedidoService service;

  @GetMapping
  public List<AlteracoesPedido> getAll() {
    return service.findAll();
  }

  @GetMapping("/{id}")
  public ResponseEntity<AlteracoesPedido> getById(@PathVariable Integer id) {
    return ResponseEntity.ok(service.findById(id));
  }

  @PostMapping
  public ResponseEntity<AlteracoesPedido> create(@RequestBody AlteracoesPedido alteracao) {
    return ResponseEntity.ok(service.save(alteracao));
  }

  @PutMapping("/{id}")
  public ResponseEntity<AlteracoesPedido> update(@PathVariable Integer id, @RequestBody AlteracoesPedido alteracao) {
    alteracao.setId(id);
    return ResponseEntity.ok(service.save(alteracao));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Integer id) {
    service.deleteById(id);
    return ResponseEntity.noContent().build();
  }
}
