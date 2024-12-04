package com.pp1.salve.model.loja;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class LojaService {

  @Autowired
  private LojaRepository repository;

  public Page<Loja> findAll(Pageable pageable) {
    return repository.findAll(pageable);
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
