package com.pp1.salve.api.pedido;
import java.util.List;

import com.pp1.salve.model.pedido.itemDoPedido.ItemPedido;

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
public class PedidoRequest {
    @NotNull
    @NotBlank
    private List<ItemPedido> itens;
    @NotNull
    private Long enderecoEntregaId;
    @NotNull
    private Double valorTotal;
    @NotNull
    private Double taxaEntrega;
    @NotNull
    private Long lojaId;
    
}
