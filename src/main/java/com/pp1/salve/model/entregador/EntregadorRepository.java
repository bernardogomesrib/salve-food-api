package com.pp1.salve.model.entregador;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EntregadorRepository extends JpaRepository<Entregador, Long> {
    
    @Query("SELECT e FROM Entregador e WHERE e.criadoPor.id = :criadoPor")
    List<Entregador> findByCriadoPorId(String criadoPor);

    List<Entregador> findByLojaId(Long lojaId);

    boolean existsByLojaId(Long lojaId);
} 