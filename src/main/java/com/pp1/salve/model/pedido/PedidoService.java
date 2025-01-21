package com.pp1.salve.model.pedido;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.pp1.salve.api.pedido.PedidoRequest;
import com.pp1.salve.model.endereco.EnderecoService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PedidoService {
    private final EnderecoService enderecoSerice;
    @Autowired
    private PedidoRepository repository;

    public Page<Pedido> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Pedido findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
    }

    public Pedido save(PedidoRequest pedido) {
        
        
        Pedido p = Pedido.builder()
            .enderecoEntrega(enderecoSerice.findById(pedido.getEnderecoEntregaId()))
            .valorTotal(pedido.getValorTotal())
            .taxaEntrega(pedido.getTaxaEntrega())
            .itens(pedido.getItens())
            .build();

        return repository.save(p);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
