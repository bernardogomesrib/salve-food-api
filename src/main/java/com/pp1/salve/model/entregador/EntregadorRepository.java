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

    @Query("SELECT e FROM Entregador e LEFT JOIN Pedido p ON e.id = p.trajetoriaEntregador.entregador.id WHERE p.status != 'A_CAMINHO' AND e.loja = :loja ORDER BY e.usuario.lastSeenAt DESC")
    List<Entregador> findEntregadorOnlineDisponivel(Loja loja);

    Optional<Entregador> findByIdAndLoja(Long id, Loja loja);
} 
