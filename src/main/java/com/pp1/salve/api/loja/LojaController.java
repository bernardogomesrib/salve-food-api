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

import io.swagger.v3.oas.annotations.Operation;
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

  @Operation(summary = "Pega a loja do usuario logado", description = "Pega a loja do usuario logado, caso não tenha uma loja retona um erro")
  @GetMapping("minha")
  public ResponseEntity<Loja> getMinhaLoja(Authentication authentication) throws Exception {
    return ResponseEntity.ok(service.findMyLoja(authentication));
  }
  
  @Operation(summary = "Pega todas as lojas", description = "Pega todas as lojas organizando por id caso não tenha lat e longi")
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

  @Operation(summary = "Pega as lojas pelo segmento", description = "Pega as lojas pelo segmento")
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

  @Operation(summary = "Pega a loja pelo id", description = "Pega a loja pelo id")
  @GetMapping("/{id}")
  public ResponseEntity<Loja> getById(@RequestParam(required = false) Double lat,
  @RequestParam(required = false) Double longi,@PathVariable Long id) throws Exception {
    Loja loja = null;
    if(lat != null && longi != null){
      loja = service.findById(id, lat, longi);
    }else{
      loja = service.findById(id);
    }
    return ResponseEntity.ok(loja);
  }

  @Operation(summary = "Cria a loja", description = "Cria a loja, é obrigatorio enviar imagem")
  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<Loja> create(@ModelAttribute @Valid LojaRequest lojaRequest, Authentication authentication)
      throws Exception {
    return ResponseEntity.ok(service.save(service.findCoordenates(lojaRequest.build()), lojaRequest.getFile(), authentication));
  }

  @Operation(summary = "Atualiza a loja", description = "Atualiza a loja, não é obrigatorio enviar imagem")
  @PreAuthorize("hasRole('dono_de_loja')")
  @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<Loja> update(@PathVariable Long id, @ModelAttribute @Valid LojaRequestEdit loja,
      Authentication authentication) throws Exception {
    return ResponseEntity.ok(service.update(id, service.findCoordenates(loja.build()), loja.getFile(), authentication));
  }

  @Operation(summary = "Deleta a loja", description = "Deleta a loja")
  @PreAuthorize("hasRole('dono_de_loja')")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id, Authentication authentication) throws Exception {
    service.deleteById(id, authentication);
    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "Posta o som da loja", description = "Posta a musica de notificação da loja")
  @PreAuthorize("hasRole('dono_de_loja')")
  @PostMapping(value = "som", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<ResponseSons> postPostaSonsDaLoja(@ModelAttribute @NotNull MultipartFile file,
      Authentication authentication) throws Exception {

    return ResponseEntity.ok().body(new ResponseSons(
        minioService.uploadFile(service.findMyLoja(authentication).getId() + "loja", "toqueMusica", file)));
  }

  @Operation(summary = "Pega o som da loja", description = "Pega a musica de notificação da loja")
  @PreAuthorize("hasRole('dono_de_loja')")
  @GetMapping("som")
  public ResponseEntity<ResponseSons> getPostaSonsDaLoja(Authentication authentication) throws Exception {
    return ResponseEntity.ok().body(new ResponseSons(
        minioService.getMusica(service.findMyLoja(authentication).getId() + "loja", "toqueMusica")));
  }



  @Operation(summary = "Atualiza o som da loja", description = "Atualiza a musica de notificação da loja")
  @PreAuthorize("hasRole('dono_de_loja')")
  @PutMapping(value = "som", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<ResponseSons> putSom(@ModelAttribute @NotNull MultipartFile file, Authentication authentication)
      throws Exception {
    return postPostaSonsDaLoja(file, authentication);
  }

}
