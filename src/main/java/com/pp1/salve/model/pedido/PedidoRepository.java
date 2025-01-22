package com.pp1.salve.model.pedido;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    @Query("SELECT p FROM Pedido p WHERE p.criadoPor.id = :id")
    List<Pedido> findByCriadoPorId(String id);
}
