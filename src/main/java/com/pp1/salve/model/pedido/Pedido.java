package com.pp1.salve.model.pedido;

import java.util.List;

import com.pp1.salve.model.baseModel.AuditEntityPedido;
import com.pp1.salve.model.endereco.Endereco;
import com.pp1.salve.model.entregador.TrajetoriaEntregador;
import com.pp1.salve.model.loja.Loja;
import com.pp1.salve.model.pedido.itemDoPedido.ItemPedido;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Pedido")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Pedido extends AuditEntityPedido{


    @ManyToOne(fetch = jakarta.persistence.FetchType.EAGER)
    @JoinColumn(name = "endereco_entrega_id", nullable = false)
    private Endereco enderecoEntrega;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Column(name = "valor_total", nullable = false)
    private Double valorTotal;

    @Column(name = "taxa_entrega", nullable = false)
    private Double taxaEntrega;

    @Column(name = "forma_pagamento", nullable = false)
    private String formaPagamento;

    @OneToMany
    private List<ItemPedido> itens;

    @ManyToOne
    private Loja loja;

    @OneToOne
    private TrajetoriaEntregador trajetoriaEntregador;

    public enum Status {
        A_PAGAR,PENDENTE, PREPARANDO, AGUARDANDO_ENTREGADOR, A_CAMINHO, ENTREGUE, CANCELADO;
    }
}
