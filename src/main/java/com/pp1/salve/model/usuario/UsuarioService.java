package com.pp1.salve.model.usuario;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

  @Autowired
  private UsuarioRepository repository;

  public List<Usuario> findAll() {
    return repository.findAll();
  }

  public Usuario findById(Long id) {
    return repository.findById(id).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
  }

  public Usuario save(Usuario user) {
    return repository.save(user);
  }

  public void deleteById(Long id) {
    repository.deleteById(id);
  }
}
