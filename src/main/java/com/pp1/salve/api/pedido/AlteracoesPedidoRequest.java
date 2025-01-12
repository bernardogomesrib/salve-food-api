package com.pp1.salve.api.pedido;



import com.pp1.salve.model.item.Item;
import com.pp1.salve.model.pedido.AlteracoesPedido;

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
public class AlteracoesPedidoRequest {
  @NotBlank
  private String nome;
  @NotNull
  private Double valor;
  @NotNull
  private Long itemId;
  
  public AlteracoesPedido build() throws Exception {
    
    return AlteracoesPedido.builder()
        .nome(nome)
        .valor(valor)
        .item(new Item(itemId))
        .build();
  }

 
}
