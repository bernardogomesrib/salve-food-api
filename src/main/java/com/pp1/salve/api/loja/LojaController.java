package com.pp1.salve.api.loja;

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

import com.pp1.salve.model.loja.Loja;
import com.pp1.salve.model.loja.LojaService;

@RestController
@RequestMapping("/loja")
@CrossOrigin
public class LojaController {

  @Autowired
  private LojaService service;

  @GetMapping
  public List<Loja> getAll() {
    return service.findAll();
  }

  @GetMapping("/{id}")
  public ResponseEntity<Loja> getById(@PathVariable Long id) {
    return ResponseEntity.ok(service.findById(id));
  }

  @PostMapping
  public ResponseEntity<Loja> create(@RequestBody Loja loja) {
    return ResponseEntity.ok(service.save(loja));
  }

  @PutMapping("/{id}")
  public ResponseEntity<Loja> update(@PathVariable Long id, @RequestBody Loja loja) {
    loja.setId(id);
    return ResponseEntity.ok(service.save(loja));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    service.deleteById(id);
    return ResponseEntity.noContent().build();
  }
}
