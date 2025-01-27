package com.pp1.salve.kc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException.BadRequest;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.pp1.salve.exceptions.ResourceNotFoundException;
import com.pp1.salve.model.usuario.UsuarioService;

import jakarta.transaction.Transactional;

@Service
public class KeycloakService {
    @Autowired
    private KcAdmin KcAdmin;
    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String issuerUri;
    @Value("${keycloak.admin.create-user-uri}")
    private String realmLinkCriacao;
    @Value("${keycloak.admin.role-uri}")
    private String roleUri;
    @Autowired
    private UsuarioService usuarioService;

    @Transactional
    public ResponseEntity<?> createAccount(String firstName, String lastName, String username, String password,
            String phone) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + getAdminAccessToken());

        String requestBody = String.format(
                "{\"firstName\":\"%s\",\"lastName\":\"%s\",\"email\":\"%s\",\"enabled\":true,\"credentials\":[{\"type\":\"password\",\"value\":\"%s\",\"temporary\":false}],\"attributes\":{\"phone\":\"%s\"}}",
                firstName, lastName, username, password, phone);
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<?> response = restTemplate.exchange(realmLinkCriacao, HttpMethod.POST, entity,
                String.class);
        return response;
    }

    public ResponseEntity<?> login(String username, String password) {
        try {
            if(usuarioService.findUsuarioByEmail(username) == null) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("message", "Usuário não encontrado!");
                errorResponse.put("status", 404);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            }

            String url = issuerUri + "/protocol/openid-connect/token";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            String requestBody = String.format(
                    "client_id=salve&grant_type=password&username=%s&password=%s",
                    username, password);

            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
            RestTemplate restTemplate = new RestTemplate();
            
            ResponseEntity<LoginResponse> response = restTemplate.postForEntity(url, entity, LoginResponse.class);
            return response;

        } catch (HttpClientErrorException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                errorResponse.put("message", "Credenciais inválidas!");
                errorResponse.put("status", 401);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
            }

            errorResponse.put("message", e.getMessage());
            errorResponse.put("status", e.getStatusCode());
            return ResponseEntity.status(e.getStatusCode()).body(errorResponse);
        }
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
            ResponseEntity<LoginResponse> response = restTemplate.postForEntity(issuerUri + "/protocol/openid-connect/token",
                    request, LoginResponse.class);
            return response;
        } catch (BadRequest e) {
            Map<String, String> map = new LinkedHashMap<>();
            map.put("error", e.getMessage().replace("400 Bad Request: [", "").replace("]", ""));
            return ResponseEntity.status(400).body(map);
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(ex.getMessage());
        }
    }

    public ResponseEntity<?> addRoleToUser(String username, String roleName) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + getAdminAccessToken());
        Role role = getRoleByName(roleName);
        if(role == null){
            throw new ResourceNotFoundException("Role de nome "+roleName+" não encontrada");
        }
        Map<String, Object> roleRepresentation = new LinkedHashMap<>();

        roleRepresentation.put("id", role.getId());
        roleRepresentation.put("name", role.getName());
        roleRepresentation.put("description", role.getDescription());
        roleRepresentation.put("composite", role.isComposite());
        roleRepresentation.put("clientRole", role.isClientRole());
        roleRepresentation.put("containerId", role.getContainerId());
        List<Map<String, Object>> requestBody = new ArrayList<>();
        requestBody.add(roleRepresentation);
        HttpEntity<List<Map<String, Object>>> entity = new HttpEntity<>(requestBody, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<?> response = restTemplate.exchange(realmLinkCriacao + "/" + username + "/role-mappings/realm",
                HttpMethod.POST, entity, Object.class);
        return response;
    }


    public ResponseEntity<?> editProfile(String email,String firstName, String lastName, String phone,Authentication authentication) {
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + getAdminAccessToken());
        Map<String, Object> requestBody = new LinkedHashMap<>();
        requestBody.put("firstName", firstName);
        requestBody.put("lastName", lastName);
        requestBody.put("email", email);
        requestBody.put("attributes", Map.of("phone", List.of(phone)));
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
        RestTemplate restTemplate = new RestTemplate();
        
        ResponseEntity<?> response = restTemplate.exchange(realmLinkCriacao + "/" + authentication.getName(), HttpMethod.PUT, entity,
                Object.class);
        return response;
    }




    public ResponseEntity<?> listAllRoles(){
        return ResponseEntity.status(200).body(KcAdmin.getRoles());
    }
    private String getAdminAccessToken() {

        return KcAdmin.getAdminAccessToken();
    }
    private Role getRoleByName(String roleName) {
        return KcAdmin.getRoleByName(roleName);
    }
}