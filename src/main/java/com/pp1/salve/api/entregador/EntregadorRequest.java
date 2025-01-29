package com.pp1.salve.api.entregador;

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
public class EntregadorRequest {

  @NotBlank(message = "O nome não pode ser vazio")
  private String email;

  @NotNull
  private Long lojaId;

}
