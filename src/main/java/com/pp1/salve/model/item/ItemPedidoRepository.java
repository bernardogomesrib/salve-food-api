package com.pp1.salve.model.item;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pp1.salve.model.pedido.itemDoPedido.ItemPedido;

public interface ItemPedidoRepository extends JpaRepository<ItemPedido, Long> {
    
}
