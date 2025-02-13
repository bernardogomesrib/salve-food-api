package com.pp1.salve.api.auth;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pp1.salve.kc.KeycloakService;
import com.pp1.salve.kc.LoginResponse;
import com.pp1.salve.model.endereco.EnderecoService;
/* import com.pp1.salve.model.mail.MailService; */
import com.pp1.salve.model.usuario.UsuarioService;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Authentication points")
public class AuthController {
	@Autowired
	private KeycloakService keycloakService;
	@Autowired
	private EnderecoService enderecoService;
	/* @Autowired */
	/* private MailService mailService; */
    @Autowired
    private UsuarioService usuarioService;

	@PostMapping("create")
	public ResponseEntity<?> postMethodName(@RequestBody @Valid AccountCreationRequest entity) {
    	ResponseEntity<?> response = keycloakService.createAccount(entity.getFirstName(), entity.getLastName(),
            	entity.getEmail(),
            	entity.getPassword(), entity.getPhoneNumber());

    	// if (response.getStatusCode().is2xxSuccessful()) {
        // 	mailService.sendWelcomeEmail(entity.getEmail(), entity.getFirstName());
    	// }
        System.out.println("First login");
        
        keycloakService.firstlogin(entity.getEmail(), entity.getPassword());
    	return response;
	}

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Login completado, cookies definidos"),
        @ApiResponse(responseCode = "401", description = "Não autorizado"),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @PostMapping("login")
    public ResponseEntity<?> postLogin(@RequestBody @Valid AuthRequest loginRequest) {
        ResponseEntity<?> responseEntity = keycloakService.login(loginRequest.getUsername(), loginRequest.getPassword());

        if(responseEntity.getStatusCode() != HttpStatus.OK) {
            return responseEntity;
        }

        LoginResponse response = (LoginResponse) responseEntity.getBody();

        Cookie refreshTokenCookie = new Cookie("refresh_token", response.getRefreshToken());
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(7 * 24 * 60 * 60);

        Cookie accessTokenCookie = new Cookie("access_token", response.getAccessToken());
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge(7 * 24 * 60 * 60);

        Cookie tokenTypeCookie = new Cookie("token_type", response.getTokenType());
        tokenTypeCookie.setHttpOnly(true);
        tokenTypeCookie.setPath("/");
        tokenTypeCookie.setMaxAge(7 * 24 * 60 * 60);

        Cookie expiresInCookie = new Cookie("expires_in", String.valueOf(response.getExpiresIn()));
        expiresInCookie.setHttpOnly(true);
        expiresInCookie.setPath("/");
        expiresInCookie.setMaxAge(7 * 24 * 60 * 60);

        return ResponseEntity.status(200)
                .header("Set-Cookie", refreshTokenCookie.toString())
                .header("Set-Cookie", accessTokenCookie.toString())
                .header("Set-Cookie", tokenTypeCookie.toString())
                .header("Set-Cookie", expiresInCookie.toString())
                .body(response);
    }

    @PostMapping("refresh")
    public ResponseEntity<?> refreshToken(@RequestBody @Valid RefreshRequest refreshtoken) {
        LoginResponse response = (LoginResponse) keycloakService.refresh(refreshtoken.getRefreshToken()).getBody();
        Cookie refreshTokenCookie = new Cookie("refresh_token", response.getRefreshToken());
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(7 * 24 * 60 * 60);

        Cookie accessTokenCookie = new Cookie("access_token", response.getAccessToken());
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge(7 * 24 * 60 * 60);

        Cookie tokenTypeCookie = new Cookie("token_type", response.getTokenType());
        tokenTypeCookie.setHttpOnly(true);
        tokenTypeCookie.setPath("/");
        tokenTypeCookie.setMaxAge(7 * 24 * 60 * 60);

        Cookie expiresInCookie = new Cookie("expires_in", String.valueOf(response.getExpiresIn()));
        expiresInCookie.setHttpOnly(true);
        expiresInCookie.setPath("/");
        expiresInCookie.setMaxAge(7 * 24 * 60 * 60);
        return ResponseEntity.status(200)
                .header("Set-Cookie", refreshTokenCookie.toString())
                .header("Set-Cookie", accessTokenCookie.toString())
                .header("Set-Cookie", tokenTypeCookie.toString())
                .header("Set-Cookie", expiresInCookie.toString())
                .body(response);
    }

    @PreAuthorize("hasRole('usuario')")
    @PostMapping("logout")
    public ResponseEntity<?> log(@AuthenticationPrincipal Jwt jwt, @RequestBody @Valid RefreshRequest refreshtoken) {
        return keycloakService.logout(refreshtoken.getRefreshToken());
    }

    @PreAuthorize("hasRole('usuario')")
    @GetMapping("introspect")
    public ResponseEntity<Map<String, Object>> getUserInfo(@AuthenticationPrincipal Jwt jwt,Authentication authentication) throws Exception {

        Map<String, Object> claims = new HashMap<>(jwt.getClaims());
        claims.put("enderecos", enderecoService.findByUsuario(authentication));
        claims.put("pfp",usuarioService.getImage(authentication));
        return ResponseEntity.ok(claims);
    }


    @PreAuthorize("hasRole('usuario')")
    @GetMapping("roles")
    public ResponseEntity<?> getAllRoles() {
        return keycloakService.listAllRoles();
    }

    @PreAuthorize("hasRole('usuario')")
    @PutMapping()
    public ResponseEntity<?> putEditarPerfil(@RequestBody EditProfileRequest entity, Authentication authentication) {
        return keycloakService.editProfile(entity.getEmail(),entity.getFirstName(), entity.getLastName(), entity.getPhone(),authentication);
    }

}
