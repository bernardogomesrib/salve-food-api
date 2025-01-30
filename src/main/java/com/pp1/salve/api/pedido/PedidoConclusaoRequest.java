package com.pp1.salve.api.pedido;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PedidoConclusaoRequest {
    @NotNull
    private Long idEntregador;
    @NotNull
    private String senha;
}
