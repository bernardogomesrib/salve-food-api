package com.pp1.salve.api.loja;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.pp1.salve.minio.MinIOInterfacing;
import com.pp1.salve.model.loja.Loja;
import com.pp1.salve.model.loja.LojaService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/loja")
@CrossOrigin
@Tag(name = "Loja", description = "pontos de interação com um Loja")
@RequiredArgsConstructor
public class LojaController {
  private final MinIOInterfacing minioService;

  private final LojaService service;
  
  @Value("${api.url}")
  private String apiBaseUrl;

  @GetMapping("minha")
  public ResponseEntity<Loja> getMinhaLoja(Authentication authentication) throws Exception {
    return ResponseEntity.ok(service.findMyLoja(authentication));
  }

  @GetMapping
  public Page<Loja> getAll(@RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(required = false) Double lat,
      @RequestParam(required = false) Double longi) throws Exception {
    Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
    if (lat != null && longi != null) {
      return service.findAll(pageable, lat, longi);
    } else {
      return service.findAll(pageable);
    }
  }

  @GetMapping("segmento/{id}")
  public Page<Loja> getViaSegmento(@PathVariable long id, @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(required = false) Double lat,
      @RequestParam(required = false) Double longi) throws Exception {
    Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
    if (lat != null && longi != null) {
      return service.findAllSegmento(id, pageable, lat, longi);
    } else {
      return service.findAllSegmento(id, pageable);
    }
  }

  @GetMapping("/{id}")
  public ResponseEntity<Loja> getById(@PathVariable Long id) {
    return ResponseEntity.ok(service.findById(id));
  }

  
  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<Loja> create(@ModelAttribute @Valid LojaRequest lojaRequest, Authentication authentication)
      throws Exception {
    return ResponseEntity.ok(service.save(service.findCoordenates(lojaRequest.build()), lojaRequest.getFile(), authentication));
  }

  @PreAuthorize("hasRole('dono_de_loja')")
  @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<Loja> update(@PathVariable Long id, @ModelAttribute @Valid LojaRequestEdit loja,
      Authentication authentication) throws Exception {
    return ResponseEntity.ok(service.update(id, service.findCoordenates(loja.build()), loja.getFile(), authentication));
  }

  @PreAuthorize("hasRole('dono_de_loja')")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id, Authentication authentication) throws Exception {
    service.deleteById(id, authentication);
    return ResponseEntity.noContent().build();
  }

  @PreAuthorize("hasRole('dono_de_loja')")
  @PostMapping(value = "som", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<ResponseSons> postPostaSonsDaLoja(@ModelAttribute @NotNull MultipartFile file,
      Authentication authentication) throws Exception {

    return ResponseEntity.ok().body(new ResponseSons(
        minioService.uploadFile(service.findMyLoja(authentication).getId() + "loja", "toqueMusica", file)));
  }

  @PreAuthorize("hasRole('dono_de_loja')")
  @GetMapping("som")
  public ResponseEntity<ResponseSons> getPostaSonsDaLoja(Authentication authentication) throws Exception {
    return ResponseEntity.ok().body(new ResponseSons(
        minioService.getSingleUrl(service.findMyLoja(authentication).getId() + "loja", "toqueMusica")));
  }

  @PreAuthorize("hasRole('dono_de_loja')")
  @PutMapping(value = "som", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<ResponseSons> putSom(@ModelAttribute @NotNull MultipartFile file, Authentication authentication)
      throws Exception {
    return postPostaSonsDaLoja(file, authentication);
  }

}
