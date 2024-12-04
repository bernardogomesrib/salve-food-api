package com.pp1.salve.api.usuario;
import com.pp1.salve.model.usuario.Usuario;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioRequest {

    private String nome;
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
            .email(email)
            .cpf(cpf)
            .rg(rg)
            .cnh(cnh)
            .tipoCnh(Usuario.TipoCnh.valueOf(tipoCnh))
            .entregador(entregador)
            .build();
    }
}
