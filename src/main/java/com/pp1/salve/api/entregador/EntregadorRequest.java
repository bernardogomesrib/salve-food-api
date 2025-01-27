package com.pp1.salve.api.entregador;

import org.springframework.web.multipart.MultipartFile;

import com.pp1.salve.model.entregador.Entregador;
import com.pp1.salve.model.loja.Loja;
import com.pp1.salve.model.usuario.Usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EntregadorRequest {

  @NotBlank(message = "O nome não pode ser vazio")
  private String firstName;

    @NotBlank(message = "O sobrenome não pode ser vazio")
    private String lastName;

    @NotBlank(message = "O email não pode ser vazio")
    @Email(message = "Email inválido")
    private String email;

    @NotBlank(message = "A senha não pode ser vazio")
    private String password;

    @NotBlank(message = "O celular não pode ser vazio")
    private String phoneNumber;

    @NotNull
    private Long lojaId;

    private MultipartFile file;

    public Entregador build(Loja loja, String userId) {
        return Entregador.builder()
                .disponivel(true)
                .loja(loja)
                .usuario(
                    Usuario.builder()
                            .id(userId)
                            .firstName(this.firstName)
                            .lastName(this.lastName)
                            .email(this.email)
                            .phone(this.phoneNumber)
                            .build()
                )
                .build();
    }
    
}
