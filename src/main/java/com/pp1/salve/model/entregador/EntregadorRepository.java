package com.pp1.salve.model.entregador;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.pp1.salve.model.loja.Loja;

public interface EntregadorRepository extends JpaRepository<Entregador, Long> {
    
    @Query("SELECT e FROM Entregador e WHERE e.criadoPor.id = :criadoPor")
    List<Entregador> findByCriadoPorId(String criadoPor);

    List<Entregador> findByLojaId(Long lojaId);

    boolean existsByLojaId(Long lojaId);

    @Query("SELECT e FROM Entregador e WHERE e.disponivel = true AND e.loja = :loja")
    List<Entregador> findEntregadorOnlineDisponivel(Loja loja);


    Optional<Entregador> findByIdAndLoja(Long id, Loja loja);
} 