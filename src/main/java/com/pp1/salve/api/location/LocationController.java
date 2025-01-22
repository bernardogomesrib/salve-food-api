package com.pp1.salve.api.location;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.client.RestTemplate;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/location")
@CrossOrigin
@Tag(name = "Location", description = "Geocodificação de endereços")
public class LocationController {

    @Value("${google.maps.api.key}")
    private String apiKey;

    @GetMapping
    public ResponseEntity<?> getCoordinates(
            @RequestParam(required = false, defaultValue = "") String rua,
            @RequestParam(required = false, defaultValue = "") String numero,
            @RequestParam(required = false, defaultValue = "") String bairro,
            @RequestParam(required = false, defaultValue = "") String cidade,
            @RequestParam(required = false, defaultValue = "") String estado
            ) {

        StringBuilder enderecoBuilder = new StringBuilder();
        if (!rua.isEmpty())
            enderecoBuilder.append(rua).append(", ");
        if (!numero.isEmpty())
            enderecoBuilder.append(numero).append(", ");
        if (!bairro.isEmpty())
            enderecoBuilder.append(bairro).append(", ");
        if (!cidade.isEmpty())
            enderecoBuilder.append(cidade).append(", ");
        if (!estado.isEmpty())
            enderecoBuilder.append(estado);

        String enderecoFormatado = enderecoBuilder.toString().trim();
        if (enderecoFormatado.endsWith(",")) {
            enderecoFormatado = enderecoFormatado.substring(0, enderecoFormatado.length() - 1);
        }

        String url = String.format(
                "https://maps.googleapis.com/maps/api/geocode/json?address=%s&key=%s",
                enderecoFormatado.replace(" ", "+"),
                apiKey);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

        Map<String, Object> responseBody = response.getBody();
        if (responseBody == null || !responseBody.containsKey("results")) {
            return ResponseEntity.badRequest().body("Endereço inválido ou não encontrado");
        }

        List<Map<String, Object>> results = (List<Map<String, Object>>) responseBody.get("results");
        if (results.isEmpty()) {
            return ResponseEntity.badRequest().body("Nenhum resultado encontrado para o endereço fornecido");
        }

        Map<String, Object> geometry = (Map<String, Object>) results.get(0).get("geometry");
        Map<String, Double> location = (Map<String, Double>) geometry.get("location");

        Double latitude = location.get("lat");
        Double longitude = location.get("lng");

        return ResponseEntity.ok(Map.of(
                "latitude", latitude,
                "longitude", longitude));
    }

}
