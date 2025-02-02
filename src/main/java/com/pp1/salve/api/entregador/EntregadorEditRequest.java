package com.pp1.salve.api.entregador;

import org.springframework.web.multipart.MultipartFile;

import com.pp1.salve.model.entregador.Entregador;
import com.pp1.salve.model.loja.Loja;

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
public class EntregadorEditRequest {

  @NotNull
  private Long id;

  @NotBlank(message = "O nome não pode ser vazio")
  private String nome;

  @NotNull
  private Long lojaId;

  private MultipartFile file;

  private Boolean disponivel;

  public Entregador build(Loja loja) {
    return Entregador.builder()
        .disponivel(true)
        .nome(nome)
        .loja(loja)
        .build();
  }
}
