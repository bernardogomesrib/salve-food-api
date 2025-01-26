package com.pp1.salve.model.reviewRestaurante;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.pp1.salve.model.loja.Loja;

public interface ReviewRestauranteRepository extends JpaRepository<ReviewRestaurante, Long> {
    Page<ReviewRestaurante> findByLoja(Loja loja, Pageable pageable);
    
    boolean existsByLojaAndCriadoPorId(Loja loja, String id);

    ReviewRestaurante findByLojaAndCriadoPorId(Loja loja, String id);

    Optional<ReviewRestaurante> findByLojaIdAndCriadoPorId(Long id, String criadoPorId);
} 
