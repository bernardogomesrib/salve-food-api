package com.pp1.salve.api.item;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.pp1.salve.model.item.Item;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequest {

  private String nome;
  private Double valor;
  private Boolean alteravel;
  private Integer lojaId;
  private Integer categoriaItemId;

  public Item build() {
    return Item.builder()
        .nome(nome)
        .valor(valor)
        .alteravel(alteravel)
        .build();
  }
}
