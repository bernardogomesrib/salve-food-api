package com.pp1.salve.api.usuario;
import com.pp1.salve.model.usuario.Usuario;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioRequest {
    @NotNull
    private String nome;
    @NotNull
    private String email;
    private String cpf;
    private String rg;
    private String cnh;
    private String tipoCnh;
    private Long lojaId;
    private Boolean entregador;

    public Usuario build() {
        return Usuario.builder()
            .nome(nome)
            .username(email)
            .cpf(cpf)
            .rg(rg)
            .cnh(cnh)
            .tipoCnh(Usuario.TipoCnh.valueOf(tipoCnh))
            .entregador(entregador)
            .build();
    }
}
