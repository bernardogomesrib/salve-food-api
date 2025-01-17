package com.pp1.salve.model.loja;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface LojaRepository extends JpaRepository<Loja, Long> {
    @Query("SELECT l FROM Loja l WHERE l.criadoPor.id = :criadoPor")
    List<Loja> findByCriadoPorId(String criadoPor);
}
