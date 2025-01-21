package com.pp1.salve.api.pedido;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.pp1.salve.model.endereco.EnderecoService;
import com.pp1.salve.model.item.itemDoPedido.ItemPedido;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PedidoRequest {

    private List<ItemPedido> itens;
    private Long enderecoEntregaId;
    private Double valorTotal;
    private Double taxaEntrega;

    @Autowired
    EnderecoService enderecoController;
}
