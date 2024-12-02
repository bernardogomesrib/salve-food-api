package com.pp1.salve.model.loja;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LojaService {

  @Autowired
  private LojaRepository repository;

  public List<Loja> findAll() {
    return repository.findAll();
  }

  public Loja findById(Integer id) {
    return repository.findById(id).orElseThrow(() -> new RuntimeException("Loja não encontrada"));
  }

  public Loja save(Loja loja) {
    return repository.save(loja);
  }

  public void deleteById(Integer id) {
    repository.deleteById(id);
  }
}
