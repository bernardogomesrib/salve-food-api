package com.pp1.salve.config.securtity;

import static org.springframework.security.config.Customizer.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(securedEnabled = true)
public class Security {
    private final AuthenticationProvider authenticationProvider;
    private final JwtFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain appSecurityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .cors(withDefaults())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .formLogin(form -> form.disable())
            .authorizeHttpRequests(authorize -> authorize
            .requestMatchers( "/produto/*",
            "/endereco/*",
            "/pedido/*",
            "/item/*",
            "/loja/*",
            "/pedido/*",
            "/swagger-ui.html",
            "/swagger-ui/**"
            ).permitAll()
            .anyRequest().authenticated())
            .addFilterBefore(jwtAuthFilter,UsernamePasswordAuthenticationFilter.class)
            .authenticationProvider(authenticationProvider);
            
        return http.build();
    }
}
