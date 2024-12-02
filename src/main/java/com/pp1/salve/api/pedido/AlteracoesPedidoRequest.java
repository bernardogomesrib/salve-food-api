package com.pp1.salve.api.pedido;

import com.pp1.salve.model.pedido.AlteracoesPedido;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlteracoesPedidoRequest {
  private String nome;
  private Double valor;
  private Integer itemId;

  public AlteracoesPedido build(){
    return AlteracoesPedido.builder()
      .nome(nome)
      .valor(valor)
      .build();
  }
}
