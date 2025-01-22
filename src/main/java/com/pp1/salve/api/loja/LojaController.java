package com.pp1.salve.api.loja;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
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
import org.springframework.web.client.RestTemplate;

import com.pp1.salve.model.loja.Loja;
import com.pp1.salve.model.loja.LojaService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/loja")
@CrossOrigin
@Tag(name = "Loja", description = "pontos de interação com um Loja")
public class LojaController {

  @Autowired
  private LojaService service;

  @Value("${application.url}")
  private String apiBaseUrl;

  @GetMapping("minha")
  public ResponseEntity<List<Loja>> getMinhaLoja(Authentication authentication) throws Exception {
    return ResponseEntity.ok(service.findMyLojas(authentication));
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

  @PostMapping(consumes = "multipart/form-data")
  public ResponseEntity<Loja> create(@ModelAttribute @Valid LojaRequest loja, Authentication authentication)
      throws Exception {
    RestTemplate restTemplate = new RestTemplate();

    String token = ((org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken) authentication)
        .getToken()
        .getTokenValue();

    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(token);
    HttpEntity<Void> entity = new HttpEntity<>(headers);

    String url = String.format(
        "%s?rua=%s&numero=%s&bairro=%s&cidade=%s&estado=%s",
        apiBaseUrl,
        loja.getRua(),
        loja.getNumero(),
        loja.getBairro(),
        loja.getCidade(),
        loja.getEstado());

    ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
    Map<String, Object> responseBody = response.getBody();

    if (responseBody == null || !responseBody.containsKey("latitude") || !responseBody.containsKey("longitude")) {
      throw new RuntimeException("Coordenadas não retornadas pelo serviço de localização");
    }

    Double latitude = (Double) responseBody.get("latitude");
    Double longitude = (Double) responseBody.get("longitude");

    Loja lojaEntity = loja.build();
    lojaEntity.setLatitude(latitude);
    lojaEntity.setLongitude(longitude);

    return ResponseEntity.ok(service.save(lojaEntity, loja.getFile(), authentication));
  }

  @PreAuthorize("hasRole('dono_de_loja')")
  @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<Loja> update(@PathVariable Long id, @ModelAttribute @Valid LojaRequest loja,
      Authentication authentication) throws Exception {
    return ResponseEntity.ok(service.update(id, loja.build(), loja.getFile(), authentication));
  }

  @PreAuthorize("hasRole('dono_de_loja')")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    service.deleteById(id);
    return ResponseEntity.noContent().build();
  }
}
