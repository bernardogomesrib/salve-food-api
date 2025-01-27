package com.pp1.salve.api.auth;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EditProfileRequest {
    private String firstName;
    private String lastName;
    private String phone;

    @Email(message = "Email inválido")
    private String email;
}
