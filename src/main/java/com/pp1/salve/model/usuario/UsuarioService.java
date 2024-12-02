package com.pp1.salve.model.usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

  @Autowired
  private UsuarioRepository repository;

  public List<Usuario> findAll() {
    return repository.findAll();
  }

  public Usuario findById(Integer id) {
    return repository.findById(id).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
  }

  public Usuario save(Usuario user) {
    return repository.save(user);
  }

  public void deleteById(Integer id) {
    repository.deleteById(id);
  }
}
