package com.pp1.salve.model.pedido.itemDoPedido;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pp1.salve.model.item.Item;
import com.pp1.salve.model.pedido.Pedido;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemPedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Item item;
    private Integer quantidade;
    private double valorUnitario;
    @JsonIgnore
    @ManyToOne
    private Pedido pedido;

}
