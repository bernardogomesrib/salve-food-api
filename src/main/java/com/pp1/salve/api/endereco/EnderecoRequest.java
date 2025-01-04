package com.pp1.salve.api.endereco;

import com.pp1.salve.model.endereco.Endereco;

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
public class EnderecoRequest {
    @NotNull
    @NotBlank
    private String rua;
    @NotNull
    @NotBlank
    private String numero;
    @NotNull
    @NotBlank
    private String complemento;
    @NotNull
    @NotBlank
    private String bairro;
    @NotNull
    @NotBlank
    private String cidade;
    @NotNull
    @NotBlank
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
