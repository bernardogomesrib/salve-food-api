package com.pp1.salve.model.pedido;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.pp1.salve.model.endereco.Endereco;
import com.pp1.salve.model.entregador.TrajetoriaEntregador;
import com.pp1.salve.model.item.ItemPedido;
import com.pp1.salve.model.usuario.Usuario;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Entity
@Table(name = "Pedido")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Usuario user;

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

    @ManyToMany
    private List<ItemPedido> items;

    @OneToOne
    private TrajetoriaEntregador trajetoriaEntregador;

    public enum Status {
        PENDENTE, PROCESSANDO, FINALIZADO, CANCELADO;
    }
}
