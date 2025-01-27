package com.pp1.salve.model.entregador;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.pp1.salve.exceptions.ResourceNotFoundException;
import com.pp1.salve.minio.MinIOInterfacing;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EntregadorService {
  private static final String ENTREGADOR = "entregador";
  private static final String ENTREGADOR_IMAGE = "entregadorImage";

  private final MinIOInterfacing minIOInterfacing;
  private final EntregadorRepository repository;

  public Entregador save(Entregador entregador, MultipartFile file, Authentication authentication) throws Exception {

    Entregador entregadorSave = repository.save(entregador);

    if (file != null && !file.isEmpty()) {
      String imagePath = minIOInterfacing.uploadFile(ENTREGADOR, entregadorSave.getId() + ENTREGADOR_IMAGE, file);
      entregadorSave.setImage(imagePath);
    }

    return repository.save(entregadorSave);
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

  public Entregador updateStatus(Long id, Boolean disponivel, Authentication authentication) throws Exception {
    Entregador entregador = repository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Entregador não encontrado"));

    entregador.setDisponivel(disponivel);
    return repository.save(entregador);
  }

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

  public Entregador monta(Entregador entregador) throws Exception {
    entregador.setImage(minIOInterfacing.getSingleUrl(ENTREGADOR, entregador.getId() + ENTREGADOR_IMAGE));
    return entregador;
  }
}
