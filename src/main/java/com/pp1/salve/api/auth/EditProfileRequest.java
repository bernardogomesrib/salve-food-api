package com.pp1.salve.api.auth;

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
    private String email;
}
