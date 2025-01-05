package com.pp1.salve.kc;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException.BadRequest;
import org.springframework.web.client.RestTemplate;

import jakarta.transaction.Transactional;

@Service
public class KeycloakService {
    @Autowired
    private KcAdmin KcAdmin;
    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String issuerUri;
    @Value("${keycloak.admin.create-user-uri}")
    private String realmLinkCriacao;

    @Transactional
    public ResponseEntity<?> createAccount(String firstName, String lastName, String username, String password,
            String email) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + getAdminAccessToken());

        String requestBody = String.format(
                "{\"firstName\":\"%s\", \"lastName\":\"%s\", \"username\": \"%s\", \"enabled\": true, \"email\": \"%s\", \"credentials\": [{\"type\": \"password\", \"value\": \"%s\", \"temporary\": false}]}",
                firstName, lastName, username, email, password);
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<?> response = restTemplate.exchange(realmLinkCriacao, HttpMethod.POST, entity,
                String.class);
        return response;
    }

    public ResponseEntity<?> login(String username, String password) {
        String url = issuerUri + "/protocol/openid-connect/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String requestBody = String.format(
                "client_id=salve&grant_type=password&username=%s&password=%s",
                username, password);

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<?> response = restTemplate.postForEntity(url, entity, Map.class);

        return response;
    }

    public ResponseEntity<?> logout(String refreshToken) {
        String url = issuerUri + "/protocol/openid-connect/logout";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String requestBody = String.format(
                "client_id=salve&refresh_token=%s",
                refreshToken);

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<?> response = restTemplate.postForEntity(url, entity, String.class);

        return response;
    }

    public ResponseEntity<?> refresh(String refreshToken) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("client_id", "salve");
        formData.add("refresh_token", refreshToken);
        formData.add("grant_type", "refresh_token");
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(formData, headers);

        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<?> response = restTemplate.postForEntity(issuerUri + "/protocol/openid-connect/token",
                    request, Map.class);
            return response;
        } catch (BadRequest e) {
            Map<String, String> map = new LinkedHashMap<>();
            map.put("error", e.getMessage().replace("400 Bad Request: [", "").replace("]", ""));
            return ResponseEntity.status(400).body(map);
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(ex.getMessage());
        }
    }

    private String getAdminAccessToken() {

        return KcAdmin.getAdminAccessToken();
    }
    
}
