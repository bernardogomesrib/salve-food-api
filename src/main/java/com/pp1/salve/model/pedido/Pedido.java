package com.pp1.salve.model.pedido;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.pp1.salve.model.baseModel.AuditEntityUserVisible;
import com.pp1.salve.model.endereco.Endereco;
import com.pp1.salve.model.entregador.TrajetoriaEntregador;
import com.pp1.salve.model.item.ItemPedido;

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
public class Pedido extends AuditEntityUserVisible {


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

    @Column(name = "data_pedido", nullable = false)
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime dataPedido;
    
    @Column(name = "data_entrega", nullable = false)
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime dataEntrega;

    @OneToMany
    private List<ItemPedido> itens;

    @OneToOne
    private TrajetoriaEntregador trajetoriaEntregador;

    public enum Status {
        PENDENTE, PREPARANDO, AGUARDANDO_ENTREGADOR, A_CAMINHO, FINALIZADO, CANCELADO;
    }
}
