package com.pp1.salve.kc;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import lombok.Data;

@Service
@Data
public class KcAdmin {

    @Value("${keycloak.admin.realm-uri}")
    private String realmLinkMaster;
    @Value("${keycloak.admin.username}")
    private String ADMIN_USERNAME;
    @Value("${keycloak.admin.password}")
    private String ADMIN_PASSWORD;
    private static String ADMIN_ACCESS_TOKEN;
    private static String ADMIN_REFRESH_TOKEN;
    private static String ADMIN_CLIENT_ID = "admin-cli";
    private static long ADMIN_TOKEN_EXPIRATION_TIME;
    
    public void loginAdmin(){
        // Implement the logic to get the admin access token from Keycloak
        // Use the ADMIN_USERNAME, ADMIN_PASSWORD, ADMIN_CLIENT_ID to get the access token
        // Use the ADMIN_ACCESS_TOKEN and ADMIN_REFRESH_TOKEN to store the tokens
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
            formData.add("client_id", ADMIN_CLIENT_ID);
            formData.add("grant_type", "password");
            formData.add("username", this.ADMIN_USERNAME);
            formData.add("password", this.ADMIN_PASSWORD);

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(formData, headers);
            RestTemplate restTemplate = new RestTemplate();
            String url = this.realmLinkMaster+"/protocol/openid-connect/token";
            System.out.println("url de login de admin: "+url);
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
            try {
                JSONObject jsonObj = new JSONObject(response.getBody());
                ADMIN_ACCESS_TOKEN = jsonObj.getString("access_token");
                ADMIN_REFRESH_TOKEN = jsonObj.getString("refresh_token");
                ADMIN_TOKEN_EXPIRATION_TIME = System.currentTimeMillis() + jsonObj.getLong("expires_in") * 1000;
            } catch (Exception e) {
                System.out.println("Error getting admin token: "+e.getMessage());
            }
    }
    public String getAdminAccessToken() {
        if(System.currentTimeMillis() > ADMIN_TOKEN_EXPIRATION_TIME) {
            loginAdmin();
        }
        return ADMIN_ACCESS_TOKEN;
    }
    public String getAdminRefreshToken() {
        if(System.currentTimeMillis() > ADMIN_TOKEN_EXPIRATION_TIME) {
            loginAdmin();
        }
        return ADMIN_REFRESH_TOKEN;
    }

}
