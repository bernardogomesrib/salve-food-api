package com.pp1.salve.api.loja;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.pp1.salve.model.loja.DiasFuncionamento;
import com.pp1.salve.model.loja.Loja;
import com.pp1.salve.model.loja.SegmentoLoja;
import com.pp1.salve.model.loja.TipoPagamento;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LojaRequestEdit {
  @NotNull
  private String nome;
  @NotNull
  private String descricao;
  @NotNull
  private String rua;
  @NotNull
  private String numero;
  @NotNull
  private String bairro;
  @NotNull
  private String cidade;
  @NotNull
  private String estado;
  @NotNull
  private Long segmentoLojaId;
  
  private MultipartFile file;
  @NotNull
  private List<DiasFuncionamento> diasFuncionamento;
  @NotNull
  private List<TipoPagamento> tiposPagamento;
  public Loja build() {
    return Loja.builder()
        .nome(nome)
        .rua(rua)
        .numero(numero)
        .bairro(bairro)
        .cidade(cidade)
        .estado(estado)
        .segmentoLoja(SegmentoLoja.builder().id(segmentoLojaId).build())
        .descricao(descricao)
        .diasFuncionamento(diasFuncionamento)
        .tiposPagamento(tiposPagamento)
        .build();
  }
}
