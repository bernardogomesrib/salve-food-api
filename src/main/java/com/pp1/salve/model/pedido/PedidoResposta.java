package com.pp1.salve.model.pedido;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PedidoResposta {
    Pedido pedido;
    Object pagamento;
}
