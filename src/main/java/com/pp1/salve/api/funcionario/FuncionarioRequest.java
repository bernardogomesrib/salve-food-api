package com.pp1.salve.api.funcionario;

import com.pp1.salve.model.funcionario.Funcionario;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FuncionarioRequest {

    private Long userId;
    private Long lojaId;
    private Boolean ativo;

    public Funcionario build() {
        return Funcionario.builder()
            .ativo(ativo)
            .build();
    }
}
