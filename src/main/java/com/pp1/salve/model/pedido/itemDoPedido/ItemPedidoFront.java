package com.pp1.salve.model.pedido.itemDoPedido;

import com.pp1.salve.model.item.Item;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ItemPedidoFront {
    private Item product;
    private Integer quantity;
    public ItemPedido toItemPedido() {
        return ItemPedido.builder()
                .item(product)
                .quantidade(quantity)
                .build();
    } 
}
