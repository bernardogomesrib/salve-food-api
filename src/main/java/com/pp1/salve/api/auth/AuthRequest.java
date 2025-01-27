package com.pp1.salve.api.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthRequest {
    @NotNull(message = "O usuario não pode ser nulo")
    @NotBlank(message = "O usuario não pode ser vazio")
    @Email(message = "Email inválido")
    private String username;
    @NotNull(message = "A senha não pode ser nula")
    @NotBlank(message = "A senha não pode ser vazio")
    private String password;
}
