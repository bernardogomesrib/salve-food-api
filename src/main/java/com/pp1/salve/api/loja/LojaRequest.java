package com.pp1.salve.api.loja;

import org.springframework.web.multipart.MultipartFile;

import com.pp1.salve.model.loja.Loja;
import com.pp1.salve.model.loja.SegmentoLoja;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LojaRequest {

  private String nome;
  private String descricao;
  private String rua;
  private String numero;
  private String bairro;
  private String cidade;
  private String estado;
  private Long segmentoLojaId;
  private double longitude;
  private double latitude;
  private MultipartFile file;
  public Loja build() {
    return Loja.builder()
        .nome(nome)
        .rua(rua)
        .numero(numero)
        .bairro(bairro)
        .cidade(cidade)
        .estado(estado)
        .segmentoLoja(SegmentoLoja.builder().id(segmentoLojaId).build())
        .longitude(longitude)
        .latitude(latitude)
        .descricao(descricao)
        .build();
  }
}
