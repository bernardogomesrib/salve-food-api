package com.pp1.salve.model.loja;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.pp1.salve.api.location.LocationController;
import com.pp1.salve.exceptions.LocationException;
import com.pp1.salve.exceptions.ResourceNotFoundException;
import com.pp1.salve.exceptions.UnauthorizedAccessException;
import com.pp1.salve.kc.KeycloakService;
import com.pp1.salve.minio.MinIOInterfacing;
import com.pp1.salve.model.item.Item;
import com.pp1.salve.model.item.ItemRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class LojaService {
  private static final String LOJA = "loja";
  private static final String LOJA_IMAGE = "lojaImage";
  private final MinIOInterfacing minIOInterfacing;
  private final ItemRepository itemRepository;
  private final LojaRepository repository;

  private final LocationController locationController;
  private final SegmentoLojaRepository segmentoLojaRepository;

  private final KeycloakService keycloakService;

  public Loja findMyLoja(Authentication authentication) throws Exception {
    Loja lojas = repository.findByCriadoPorId(authentication.getName());
    if (lojas == null)
      throw new EntityNotFoundException("Loja não encontrada");
    return monta(lojas);
  }

  public Loja findMyLojaNoFile(Authentication authentication) {
    Loja loja = repository.findByCriadoPorId(authentication.getName());
    if (loja == null)
      throw new EntityNotFoundException("Loja não encontrada");
    return loja;
  }

  public Page<Loja> findAll(Pageable pageable) throws Exception {
    Page<Loja> loja = repository.findAll(pageable);
    for (Loja l : loja) {
      l = monta(l);
    }
    return loja;
  }

  public Page<Loja> findAll(Pageable pageable, double lat, double longi) throws Exception {
    Page<Loja> loja = repository.findAll(pageable, lat, longi);
    for (Loja l : loja) {
      l = monta(l, lat, longi);
    }
    return loja;
  }

  public Page<Loja> findAllSegmento(Long id, Pageable pageable) throws Exception {
    Page<Loja> loja = repository.findBySegmentoLojaId(id, pageable);
    for (Loja l : loja) {
      l = monta(l);
    }
    return loja;
  }

  public Page<Loja> findAllSegmento(Long id, Pageable pageable, double lat, double longi) throws Exception {
    Page<Loja> loja = repository.findBySegmentoLojaId(id, pageable, lat, longi);
    for (Loja l : loja) {
      l = monta(l, lat, longi);
    }
    return loja;
  }

  public Loja findById(Long id) {
    return repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Loja não encontrada"));
  }

  public Loja save(Loja loja, @NotNull MultipartFile file, Authentication authentication) throws Exception {
    Loja lojaDoUsuario = repository.findByCriadoPorId(authentication.getName());
    if (lojaDoUsuario != null) {
      if (lojaDoUsuario.isAtivo() == true) {
        throw new UnauthorizedAccessException("Você já possui uma loja cadastrada.");
      } else {
        keycloakService.addRoleToUser(authentication.getName(), "dono_de_loja");

        SegmentoLoja segmentoLoja = segmentoLojaRepository.findById(loja.getSegmentoLoja().getId())
            .orElseThrow(() -> new EntityNotFoundException(
                "Segmento de Loja não encontrado com ID: " + loja.getSegmentoLoja().getId()));
        lojaDoUsuario.setSegmentoLoja(segmentoLoja);
        lojaDoUsuario.setAtivo(true);
        lojaDoUsuario.setDiasFuncionamento(loja.getDiasFuncionamento());
        lojaDoUsuario.setNome(loja.getNome());
        lojaDoUsuario.setDescricao(loja.getDescricao());
        lojaDoUsuario.setRua(loja.getRua());
        lojaDoUsuario.setNumero(loja.getNumero());
        lojaDoUsuario.setBairro(loja.getBairro());
        lojaDoUsuario.setCidade(loja.getCidade());
        lojaDoUsuario.setEstado(loja.getEstado());
        lojaDoUsuario.setLatitude(loja.getLatitude());
        lojaDoUsuario.setLongitude(loja.getLongitude());
        lojaDoUsuario.setTiposPagamento(loja.getTiposPagamento());
        lojaDoUsuario = repository.save(lojaDoUsuario);
        lojaDoUsuario.setImage(minIOInterfacing.uploadFile(lojaDoUsuario.getId() + LOJA, LOJA_IMAGE, file));
        return repository.save(lojaDoUsuario);
      }
    }
    keycloakService.addRoleToUser(authentication.getName(), "dono_de_loja");

    SegmentoLoja segmentoLoja = segmentoLojaRepository.findById(loja.getSegmentoLoja().getId())
        .orElseThrow(() -> new EntityNotFoundException(
            "Segmento de Loja não encontrado com ID: " + loja.getSegmentoLoja().getId()));
    loja.setSegmentoLoja(segmentoLoja);
    Loja lojaSalva = repository.save(loja);
    lojaSalva.setImage(minIOInterfacing.uploadFile(lojaSalva.getId() + LOJA, LOJA_IMAGE, file));
    return repository.save(lojaSalva);

  }

  @Transactional(rollbackFor = Exception.class)
  public Loja update(Long id, Loja loja, MultipartFile file, Authentication authentication) throws Exception {
    Optional<Loja> lojaOptional = repository.findById(id);
    if (lojaOptional.isEmpty()) {
      throw new EntityNotFoundException("Loja não encontrada");
    } else {
      if (!lojaOptional.get().getCriadoPor().getId().equals(authentication.getName()))
        throw new UnauthorizedAccessException("Você não tem autoridade de modificar esta loja.");
    }
    final long ids = loja.getSegmentoLoja().getId();
    SegmentoLoja segmentoLoja = segmentoLojaRepository.findById(ids)
        .orElseThrow(() -> new EntityNotFoundException(
            "Segmento de Loja não encontrado com ID: " + ids));

    Loja lojaLocal = lojaOptional.get();
    lojaLocal.setBairro(loja.getBairro());
    lojaLocal.setCidade(loja.getCidade());
    lojaLocal.setDescricao(loja.getDescricao());
    lojaLocal.setEstado(loja.getEstado());
    lojaLocal.setLatitude(loja.getLatitude());
    lojaLocal.setLongitude(loja.getLongitude());
    lojaLocal.setNome(loja.getNome());
    lojaLocal.setNumero(loja.getNumero());
    lojaLocal.setRua(loja.getRua());
    lojaLocal.setTiposPagamento(loja.getTiposPagamento());
    lojaLocal.setDiasFuncionamento(loja.getDiasFuncionamento());
    lojaLocal.setSegmentoLoja(segmentoLoja);

    if (file != null) {
      log.info("File is not null");
      loja = repository.save(lojaLocal);
      loja.setImage(minIOInterfacing.uploadFile(getUniqueName(lojaLocal.getId()), LOJA_IMAGE, file));
      return loja;
    }
    return monta(repository.save(lojaLocal));
  }

  public void deleteById(Long id, Authentication authentication) throws Exception {
    Loja i = repository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Loja não encontrada com id: " + id));
    if (!i.getCriadoPor().getId().equals(authentication.getName())) {
      throw new UnauthorizedAccessException("Você não tem permissão para deletar esta loja");
    }

    i.setAtivo(false);
    repository.save(i);
    List<Item> itens = itemRepository.findByLoja(i);
    for (Item item : itens) {
      try {
        itemRepository.delete(item);
        minIOInterfacing.deleteFile(i.getId() + "loja", item.getId().toString());
      } catch (org.springframework.dao.DataIntegrityViolationException e) {
        item.setAtivo(false);
        itemRepository.save(item);
      }
    }
    try {
      repository.delete(i);
      minIOInterfacing.deleteBucket(i.getId() + "loja");
    } catch (org.springframework.dao.DataIntegrityViolationException e) {
      return;
    }
  }

  public Loja monta(Loja loja) throws Exception {
    loja.setImage(minIOInterfacing.getSingleUrl(getUniqueName(loja.getId()), LOJA_IMAGE));
    return loja;
  }

  public Loja monta(Loja loja, double lati, double longi) throws Exception {
    loja.setImage(minIOInterfacing.getSingleUrl(getUniqueName(loja.getId()), LOJA_IMAGE));
    loja.setDeliveryTime(deliveryTimeCalculator(loja, lati, longi));
    return loja;
  }

  public double deliveryTimeCalculator(Loja loja, double latitude, double longitude) {
    return calculateTravelTime(loja, latitude, longitude);
  }

  public static double calculateTravelTime(Loja loja, double userLatitude,
      double userLongitude) {
    // Raio médio da Terra em quilômetros
    final double EARTH_RADIUS_KM = 6371.0;

    double storeLatitude = loja.getLatitude();
    double storeLongitude = loja.getLongitude();

    // Converter as latitudes e longitudes de graus para radianos
    double latDistance = Math.toRadians(storeLatitude - userLatitude);
    double lonDistance = Math.toRadians(storeLongitude - userLongitude);

    double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
        + Math.cos(Math.toRadians(userLatitude)) * Math.cos(Math.toRadians(storeLatitude))
            * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

    // Distância em quilômetros
    double distance = EARTH_RADIUS_KM * c;
    loja.setDistancia(distance);
    // Tempo de viagem em horas
    double travelTimeHours = distance / 15;

    // Converter o tempo de viagem para minutos
    return travelTimeHours * 60;
  }

  private String getUniqueName(Long id) {
    return id + "" + LOJA;
  }

  public Loja findCoordenates(Loja loja) {
    try {

      @SuppressWarnings("unchecked")
      Map<String, Object> cordinates = (Map<String, Object>) locationController
          .getCoordinates(loja.getRua(), loja.getNumero(), loja.getBairro(), loja.getCidade(), loja.getEstado())
          .getBody();

      if (cordinates == null || !cordinates.containsKey("latitude") || !cordinates.containsKey("longitude")) {
        throw new RuntimeException("Coordenadas não retornadas pelo serviço de localização");
      }

      Double latitude = (Double) cordinates.get("latitude");
      Double longitude = (Double) cordinates.get("longitude");

      loja.setLatitude(latitude);
      loja.setLongitude(longitude);
    } catch (Exception e) {
      throw new LocationException("não foi possível pegar a localização com os dados informados");
    }
    return loja;
  }

}
