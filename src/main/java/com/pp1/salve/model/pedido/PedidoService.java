package com.pp1.salve.model.pedido; 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository repository;

    public List<Pedido> findAll() {
        return repository.findAll();
    }

    public Pedido findById(Integer id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
    }

    public Pedido save(Pedido pedido) {
        return repository.save(pedido);
    }

    public void deleteById(Integer id) {
        repository.deleteById(id);
    }
}
