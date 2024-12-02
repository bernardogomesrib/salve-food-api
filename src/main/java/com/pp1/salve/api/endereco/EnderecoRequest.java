package com.pp1.salve.api.endereco;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.pp1.salve.model.endereco.Endereco;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnderecoRequest {

    private Integer userId;
    private String rua;
    private String numero;
    private String complemento;
    private String bairro;
    private String cidade;
    private String estado;

    public Endereco build() {
        return Endereco.builder()
            .rua(rua)
            .numero(numero)
            .complemento(complemento)
            .bairro(bairro)
            .cidade(cidade)
            .estado(estado)
            .build();
    }
}
