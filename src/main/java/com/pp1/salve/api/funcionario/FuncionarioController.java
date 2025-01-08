package com.pp1.salve.api.funcionario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pp1.salve.model.funcionario.Funcionario;
import com.pp1.salve.model.funcionario.FuncionarioService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/funcionario")
@CrossOrigin
@Tag(name = "Funcionário", description = "pontos de interação com Funcionário")
public class FuncionarioController {

  @Autowired
  private FuncionarioService service;

  @GetMapping
  public Page<Funcionario> getAll(@RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size) throws Exception {
    Pageable pageable = PageRequest.of(page, size,Sort.by("id").descending());
    return service.findAll(pageable);
  }

  @GetMapping("/{id}")
  public Funcionario getById(@PathVariable Long id) {
    return service.findById(id);
  }

  @PostMapping
  public Funcionario create(@RequestBody Funcionario funcionario) {
    return service.save(funcionario);
  }

  @PutMapping("/{id}")
  public Funcionario update(@PathVariable Long id, @RequestBody Funcionario funcionario) {
    funcionario.setId(id);
    return service.save(funcionario);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    service.deleteById(id);
    return ResponseEntity.noContent().build();
  }
}
