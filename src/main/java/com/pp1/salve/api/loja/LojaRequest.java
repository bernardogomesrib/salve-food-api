package com.pp1.salve.api.loja;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.pp1.salve.model.loja.Loja;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LojaRequest {

  private String nome;
  private String rua;
  private String numero;
  private String bairro;
  private String cidade;
  private String estado;
  private Integer segmentoLojaId;
  private String longitude;
  private String latitude;

  public Loja build() {
    return Loja.builder()
        .nome(nome)
        .rua(rua)
        .numero(numero)
        .bairro(bairro)
        .cidade(cidade)
        .estado(estado)
        .longitude(longitude)
        .latitude(latitude)
        .build();
  }
}
