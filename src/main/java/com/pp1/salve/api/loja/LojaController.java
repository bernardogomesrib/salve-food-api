package com.pp1.salve.api.loja;

import com.pp1.salve.model.loja.Loja;
import com.pp1.salve.model.loja.LojaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
  public ResponseEntity<Loja> getById(@PathVariable Integer id) {
    return ResponseEntity.ok(service.findById(id));
  }

  @PostMapping
  public ResponseEntity<Loja> create(@RequestBody Loja loja) {
    return ResponseEntity.ok(service.save(loja));
  }

  @PutMapping("/{id}")
  public ResponseEntity<Loja> update(@PathVariable Integer id, @RequestBody Loja loja) {
    loja.setId(id);
    return ResponseEntity.ok(service.save(loja));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Integer id) {
    service.deleteById(id);
    return ResponseEntity.noContent().build();
  }
}
