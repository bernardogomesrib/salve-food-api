package com.pp1.salve.api.item;

import org.springframework.web.multipart.MultipartFile;

import com.pp1.salve.model.item.Item;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequest {

  private String nome;
  private Double valor;
  private Boolean alteravel;
  private Long lojaId;
  private Long categoriaItemId;
  private MultipartFile file;
  public Item build() {
    return Item.builder()
        .nome(nome)
        .valor(valor)
        .alteravel(alteravel)
        .build();
  }
}
