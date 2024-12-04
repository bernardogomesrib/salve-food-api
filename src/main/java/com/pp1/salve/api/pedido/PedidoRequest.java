package com.pp1.salve.api.pedido;
import org.springframework.beans.factory.annotation.Autowired;

import com.pp1.salve.model.endereco.Endereco;
import com.pp1.salve.model.endereco.EnderecoService;
import com.pp1.salve.model.pedido.Pedido;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PedidoRequest {

    private Long userId;
    private Long enderecoEntregaId;
    private String status;
    private Double valorTotal;
    private Double taxaEntrega;
    @Autowired
    EnderecoService enderecoController;
    public Pedido build() {
        Endereco endereco = enderecoController.findById(enderecoEntregaId);
        return Pedido.builder()
            .userId(userId)
            .enderecoEntrega(endereco)
            .status(Pedido.Status.valueOf(status))
            .valorTotal(valorTotal)
            .taxaEntrega(taxaEntrega)
            .build();
    }
}
