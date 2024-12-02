package com.pp1.salve.api.funcionario;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.pp1.salve.model.funcionario.Funcionario;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FuncionarioRequest {

    private Integer userId;
    private Integer lojaId;
    private Boolean ativo;

    public Funcionario build() {
        return Funcionario.builder()
            .ativo(ativo)
            .build();
    }
}
