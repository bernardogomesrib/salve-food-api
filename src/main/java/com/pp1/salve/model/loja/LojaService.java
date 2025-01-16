package com.pp1.salve.model.loja;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.pp1.salve.minio.MinIOInterfacing;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LojaService {
  private final MinIOInterfacing minIOInterfacing;
  @Autowired
  private LojaRepository repository;

  @Autowired
  private SegmentoLojaRepository segmentoLojaRepository;

  public Page<Loja> findAll(Pageable pageable) throws Exception {
    Page<Loja> loja = repository.findAll(pageable);
    for (Loja l : loja) {
      l = monta(l);
    }
    return loja;
  }

  public Page<Loja> findAll(Pageable pageable, double lat, double longi) throws Exception {
    Page<Loja> loja = repository.findAll(pageable);
    for (Loja l : loja) {
      l = monta(l, lat, longi);
    }
    return loja;
  }

  public Loja findById(Long id) {
    return repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Loja não encontrada"));
  }

  public Loja save(Loja loja, MultipartFile file) throws Exception {
    SegmentoLoja segmentoLoja = segmentoLojaRepository.findById(loja.getSegmentoLoja().getId())
        .orElseThrow(() -> new EntityNotFoundException(
            "Segmento de Loja não encontrado com ID: " + loja.getSegmentoLoja().getId()));
    loja.setSegmentoLoja(segmentoLoja);
    Loja lojaSalva = repository.save(loja);
    lojaSalva.setImage(minIOInterfacing.uploadFile(lojaSalva.getId() + "loja", "lojaImage", file));
    return repository.save(lojaSalva);
  }

  public Loja update(Long id, Loja loja, MultipartFile file) throws Exception {
    loja.setId(id);
    if (file != null) {
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

  public Loja monta(Loja loja, double lati, double longi) throws Exception {
    loja.setImage(minIOInterfacing.getSingleUrl(loja.getId() + "loja", "lojaImage"));
    loja.setDeliveryTime(deliveryTimeCalculator(loja, lati, longi));
    return loja;
  }

  public double deliveryTimeCalculator(Loja loja, double latitude, double longitude) {
    double lojaLatitude = loja.getLatitude();
    double lojaLongitude = loja.getLongitude();

    return calculateTravelTime(lojaLatitude, lojaLongitude, latitude, longitude);
}
  //TODO: encontrar onde no código do projeto inteiro que está invertido, tive que inverter latitude e longitude da loja para que funcionasse corretamente o calculo de distancia
  // passei em 2 gpts diferentes e ambos só reclamaram do ponto em que eu inverti de propósito para que funcionasse
  public static double calculateTravelTime(double storeLongitude , double storeLatitude, double userLatitude, double userLongitude) {
    // Raio médio da Terra em quilômetros
    final double EARTH_RADIUS_KM = 6371.0;

    // Converter as latitudes e longitudes de graus para radianos
    double latDistance = Math.toRadians(storeLatitude - userLatitude);
    double lonDistance = Math.toRadians(storeLongitude - userLongitude);

    double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
        + Math.cos(Math.toRadians(userLatitude)) * Math.cos(Math.toRadians(storeLatitude))
            * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

    // Distância em quilômetros
    double distance = EARTH_RADIUS_KM * c;

    // Tempo de viagem em horas
    double travelTimeHours = distance / 15;

    // Converter o tempo de viagem para minutos
    return travelTimeHours * 60;
}

}
