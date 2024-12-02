package com.pp1.salve.api.funcionario;

import com.pp1.salve.model.funcionario.Funcionario;
import com.pp1.salve.model.funcionario.FuncionarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/funcionario")
@CrossOrigin
public class FuncionarioController {

  @Autowired
  private FuncionarioService service;

  @GetMapping
  public List<Funcionario> getAll() {
    return service.findAll();
  }

  @GetMapping("/{id}")
  public ResponseEntity<Funcionario> getById(@PathVariable Integer id) {
    return ResponseEntity.ok(service.findById(id));
  }

  @PostMapping
  public ResponseEntity<Funcionario> create(@RequestBody Funcionario funcionario) {
    return ResponseEntity.ok(service.save(funcionario));
  }

  @PutMapping("/{id}")
  public ResponseEntity<Funcionario> update(@PathVariable Integer id, @RequestBody Funcionario funcionario) {
    funcionario.setId(id);
    return ResponseEntity.ok(service.save(funcionario));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Integer id) {
    service.deleteById(id);
    return ResponseEntity.noContent().build();
  }
}
