package com.pp1.salve.model.entregador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface TrajetoriaEntregadorRepository extends JpaRepository<TrajetoriaEntregador, Long> {
    
}