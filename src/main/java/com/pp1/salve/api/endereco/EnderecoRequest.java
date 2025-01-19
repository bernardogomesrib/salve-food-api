package com.pp1.salve.api.endereco;

import com.pp1.salve.model.endereco.Endereco;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
    @Size(max = 255)
    private String rua;
    @NotNull
    @NotBlank
    @Size(max = 255)
    private String numero;
    @NotNull
    @Size(max = 255)
    private String complemento;
    @NotNull
    @NotBlank
    @Size(max = 255, min = 2)
    private String bairro;
    @NotNull
    @NotBlank
    @Size(max = 255, min = 2)
    private String cidade;
    @NotNull
    @NotBlank
    @Size(max = 255, min = 2)
    private String estado;
    @NotNull
    private double latitude;
    @NotNull
    private double longitude;
    @NotNull
    @Size(min= 8,max = 10)
    private String cep;
    @Size(min = 2,max = 45)
    private String apelido;

    public Endereco build() {
        return Endereco.builder()
            .rua(rua)
            .numero(numero)
            .complemento(complemento)
            .bairro(bairro)
            .cidade(cidade)
            .estado(estado)
            .latitude(latitude)
            .longitude(longitude)
            .cep(cep)
            .apelido(apelido)
            .build();
    }
}
