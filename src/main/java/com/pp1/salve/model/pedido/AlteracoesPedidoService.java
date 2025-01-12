package com.pp1.salve.model.pedido;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pp1.salve.model.item.ItemService;

import jakarta.transaction.Transactional;

@Service
public class AlteracoesPedidoService {

  @Autowired
  private AlteracoesPedidoRepository repository;
  @Autowired
  private ItemService itemService;
  public List<AlteracoesPedido> findAll() {
    return repository.findAll();
  }

  public AlteracoesPedido findById(Long id) {
    return repository.findById(id).orElseThrow(() -> new RuntimeException("Alteração não encontrada"));
  }

  @Transactional
  public AlteracoesPedido save(AlteracoesPedido alteracao) throws Exception {
    alteracao.setItem(itemService.findById(alteracao.getItem().getId()));
    return repository.save(alteracao);
  }
  
  @Transactional
  public void deleteById(Long id) {
    repository.deleteById(id);
  }
}
