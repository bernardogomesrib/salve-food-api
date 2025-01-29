package com.pp1.salve.model.entregador;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.pp1.salve.exceptions.ResourceNotFoundException;
import com.pp1.salve.kc.KeycloakService;
import com.pp1.salve.minio.MinIOInterfacing;
import com.pp1.salve.model.loja.Loja;
import com.pp1.salve.model.loja.LojaService;
import com.pp1.salve.model.usuario.Usuario;
import com.pp1.salve.model.usuario.UsuarioService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EntregadorService {
  private final LojaService lojaService;
  private final UsuarioService usuarioService;
  private final MinIOInterfacing minIOInterfacing;
  private final EntregadorRepository repository;
  private final KeycloakService keycloakService;

  public Entregador save(String email, Authentication authentication) throws Exception {
    Loja loja = lojaService.findMyLojaNoFile(authentication);
    Usuario usuario = usuarioService.findUsuarioByEmail(email);
    if (usuario == null) {
      throw new ResourceNotFoundException("Usuário não encontrado");
    }
    keycloakService.addRoleToUser(usuario.getId(), "entregador");

    Entregador entregador = Entregador.builder().loja(loja).usuario(usuario).build();

    return repository.save(entregador);
  }

  public List<Entregador> findByLoja(Long lojaId) throws Exception {
    List<Entregador> entregador = repository.findByLojaId(lojaId);

    if (!repository.existsByLojaId(lojaId)) {
      throw new ResourceNotFoundException("Loja não encontrada com o ID fornecido: " + lojaId);
    }
    for (Entregador e : entregador) {
      e = monta(e);
    }
    return entregador;
  }

  /*
   * public Entregador updateStatus(Long id, Boolean disponivel, Authentication
   * authentication) throws Exception {
   * Entregador entregador = repository.findById(id)
   * .orElseThrow(() -> new
   * ResourceNotFoundException("Entregador não encontrado"));
   * 
   * entregador.setDisponivel(disponivel);
   * return repository.save(entregador);
   * }
   */

  public void delete(Long id, Authentication authentication) throws Exception {
    repository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Entregador não encontrado"));
    repository.deleteById(id);
  }

  public Entregador findById(Long id) throws Exception {
    Entregador entregador = repository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Entregador não encontrado"));
    entregador = monta(entregador);
    return entregador;
  }

  public List<Entregador> findMeusEntregadoresDisponiveis(Authentication authentication) throws Exception {
    return monta(repository.findEntregadorOnlineDisponivel(lojaService.findMyLojaNoFile(authentication)));
  }

  public Entregador monta(Entregador entregador) throws Exception {
    entregador.setImage(minIOInterfacing.pegarImagemDePerfil(entregador.getUsuario().getId(), "pfp"));
    return entregador;
  }

  public List<Entregador> monta(List<Entregador> entregadores) throws Exception {
    for (Entregador entregador : entregadores) {
      entregador = monta(entregador);
    }
    return entregadores;
  }

  public Entregador findMeuEntregador(Long id, Loja loja) {
    return repository.findByIdAndLoja(id, loja)
        .orElseThrow(() -> new ResourceNotFoundException("Entregador não encontrado na loja"));
  }
}
