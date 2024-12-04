package com.pp1.salve.model.loja;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LojaService {

  @Autowired
  private LojaRepository repository;

  public List<Loja> findAll() {
    return repository.findAll();
  }

  public Loja findById(Long id) {
    return repository.findById(id).orElseThrow(() -> new RuntimeException("Loja não encontrada"));
  }

  public Loja save(Loja loja) {
    return repository.save(loja);
  }

  public void deleteById(Long id) {
    repository.deleteById(id);
  }
}
