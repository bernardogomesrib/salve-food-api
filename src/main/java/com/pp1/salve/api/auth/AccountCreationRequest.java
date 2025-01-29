package com.pp1.salve.api.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AccountCreationRequest {
  @NotNull(message = "O nome não pode ser nulo")
  @NotBlank(message = "O nome não pode ser vazio")
    private String firstName;
    @NotNull(message = "O sobrenome não pode ser nulo")
    @NotBlank(message = "O sobrenome não pode ser vazio")
    private String lastName;
    @NotNull(message = "O email não pode ser nulo")
    @NotBlank(message = "O email não pode ser vazio")
    @Email(message = "Email inválido")
    private String email;
    @NotNull(message = "A senha não pode ser nula")
    @NotBlank(message = "A senha não pode ser vazio")
    private String password;
    @NotNull(message = "O celular não pode ser nulo")
    private String phoneNumber;
}
