package com.pp1.salve.kc;

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
    public boolean createAccount(String firstName, String lastName, String username, String password, String email) {

        System.out.println("url de criação de usuario: " + realmLinkCriacao);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + getAdminAccessToken());

        String requestBody = String.format(
                "{\"firstName\":\"%s\", \"lastName\":\"%s\", \"username\": \"%s\", \"enabled\": true, \"email\": \"%s\", \"credentials\": [{\"type\": \"password\", \"value\": \"%s\", \"temporary\": false}]}",
                firstName, lastName, username, email, password);
        try {
            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.exchange(realmLinkCriacao, HttpMethod.POST, entity,
                    String.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            System.out.println("Error creating user: " + e.getMessage());
            return false;
        }
    }

    public String login(String username, String password) {
        String url = issuerUri + "/protocol/openid-connect/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String requestBody = String.format(
                "client_id=salve&grant_type=password&username=%s&password=%s",
                username, password);

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

        return response.getBody();
    }

    public String logout(String refreshToken) {
        String url = issuerUri + "/protocol/openid-connect/logout";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String requestBody = String.format(
                "client_id=salve&refresh_token=%s",
                refreshToken);

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

        return response.getBody();
    }

    public Object refresh(String refreshToken) {
        System.out.println("url de refresh: "+issuerUri+"/protocol/openid-connect/token");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("client_id", "salve");
        formData.add("refresh_token", refreshToken);
        formData.add("grant_type", "refresh_token");
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(formData, headers);
        
        System.out.println(request.toString());

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(issuerUri + "/protocol/openid-connect/token",
                request, String.class);
        return response.getBody();
    }

    private String getAdminAccessToken() {

        return KcAdmin.getAdminAccessToken();
    }

}
