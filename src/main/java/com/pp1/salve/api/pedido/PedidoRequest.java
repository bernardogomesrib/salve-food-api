package com.pp1.salve.api.pedido;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.pp1.salve.model.pedido.Pedido;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PedidoRequest {

    private Integer userId;
    private Integer enderecoEntregaId;
    private String status;
    private Double valorTotal;
    private Double taxaEntrega;

    public Pedido build() {
        return Pedido.builder()
            .userId(userId)
            .enderecoEntregaId(enderecoEntregaId)
            .status(Pedido.Status.valueOf(status))
            .valorTotal(valorTotal)
            .taxaEntrega(taxaEntrega)
            .build();
    }
}
