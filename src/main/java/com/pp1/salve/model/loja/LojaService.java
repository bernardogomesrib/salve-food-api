package com.pp1.salve.model.loja;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.pp1.salve.minio.MinIOInterfacing;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LojaService {
  private final MinIOInterfacing minIOInterfacing;
  @Autowired
  private LojaRepository repository;

  public Page<Loja> findAll(Pageable pageable) throws Exception {
    Page<Loja> loja = repository.findAll(pageable);
    for (Loja l : loja) {
      l = monta(l);
    }
    return loja;
  }

  public Loja findById(Long id) {
    return repository.findById(id).orElseThrow(() -> new RuntimeException("Loja não encontrada"));
  }

  public Loja save(Loja loja, MultipartFile file) throws Exception {
    Loja lojaSalva = repository.save(loja);
    lojaSalva.setImage(minIOInterfacing.uploadFile(lojaSalva.getId() + "loja", "lojaImage", file));
    return repository.save(loja);
  }

  public Loja update(Long id, Loja loja, MultipartFile file) throws Exception {
    loja.setId(id);
    if(file != null) {
      loja.setImage(minIOInterfacing.uploadFile(loja.getId() + "loja", "lojaImage", file));
    }
    return monta(repository.save(loja));
  }

  public void deleteById(Long id) {
    repository.deleteById(id);
  }

  public Loja monta(Loja loja) throws Exception {
    loja.setImage(minIOInterfacing.getSingleUrl(loja.getId() + "loja", "lojaImage"));
    return loja;
  }
}
