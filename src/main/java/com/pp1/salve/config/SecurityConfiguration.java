package com.pp1.salve.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.pp1.salve.config.auditAware.ApplicationAuditorAware;
import com.pp1.salve.config.securtity.JwtAuthenticationFilter;
import com.pp1.salve.model.usuario.Role;
import com.pp1.salve.model.usuario.Usuario;

@Configuration
@EnableWebSecurity
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class SecurityConfiguration {
        
        private final AuthenticationProvider authenticationProvider;
        private final JwtAuthenticationFilter jwtAuthenticationFilter;
        
        public SecurityConfiguration(JwtAuthenticationFilter jwtAuthenticationFilter,
        AuthenticationProvider authenticationProvider) {
                this.authenticationProvider = authenticationProvider;
                this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        }
        
        @Bean
        public AuditorAware<Usuario> auditorProvider() {
                return new ApplicationAuditorAware();
        }
        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

                http
                                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                                .csrf(c -> c.disable())
                                .csrf(AbstractHttpConfigurer::disable)
                                .authorizeHttpRequests(authorize -> authorize

                                                .requestMatchers(HttpMethod.GET, "/api/produto/")
                                                .hasAnyAuthority(Role.ROLE_CLIENTE, Role.ROLE_FUNCIONARIO_ADMIN,
                                                                Role.ROLE_FUNCIONARIO_USER) // Consulta de produto
                                                .requestMatchers(HttpMethod.POST, "/api/produto")
                                                .hasAnyAuthority(Role.ROLE_FUNCIONARIO_ADMIN,
                                                                Role.ROLE_FUNCIONARIO_USER) // Cadastro de
                                                                                            // produto
                                                .requestMatchers(HttpMethod.PUT, "/api/produto/*")
                                                .hasAnyAuthority(Role.ROLE_FUNCIONARIO_ADMIN,
                                                                Role.ROLE_FUNCIONARIO_USER) // Alteração de
                                                                                            // produto
                                                .requestMatchers(HttpMethod.DELETE, "/api/produto/*")
                                                .hasAnyAuthority(Role.ROLE_FUNCIONARIO_ADMIN) // Exclusão de produto

                                                .requestMatchers(HttpMethod.POST, "/api/funcionario").permitAll()

                                                .requestMatchers(HttpMethod.POST, "/api/cliente", "/api/auth")
                                                .permitAll()
                                                .requestMatchers(HttpMethod.GET,
                                                                "/api-docs/*",
                                                                "/swagger-ui/*")
                                                .permitAll()
                                                .anyRequest().authenticated())
                                .sessionManagement((session) -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .authenticationProvider(authenticationProvider)
                                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

                return http.build();
        }

        public CorsConfigurationSource corsConfigurationSource() {

                CorsConfiguration configuration = new CorsConfiguration();

                configuration.setAllowedOrigins(
                                Arrays.asList("http://localhost:3000", "http://localhost:8080", "http://localhost:*",
                                                "localhost:*"));
                configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
                configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
                configuration.setAllowCredentials(true);

                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", configuration);
                return source;
        }

}