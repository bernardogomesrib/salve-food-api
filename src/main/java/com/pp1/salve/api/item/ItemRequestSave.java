package com.pp1.salve.api.item;

import org.springframework.web.multipart.MultipartFile;

import com.pp1.salve.model.item.Item;

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
public class ItemRequestSave {
  @NotBlank
  @NotNull
  private String descricao;
  @NotBlank
  private String nome;
  @NotNull
  private Double valor;
  @NotNull
  private Long lojaId;
  @NotNull
  private Long categoriaItemId;
  @NotNull
  private MultipartFile file;

  public Item build() {
    return Item.builder()
        .nome(nome)
        .valor(valor)
        .descricao(descricao)
        .build();
  }
}
