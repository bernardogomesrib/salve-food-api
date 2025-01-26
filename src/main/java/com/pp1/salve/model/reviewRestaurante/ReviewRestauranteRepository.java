package com.pp1.salve.model.reviewRestaurante;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.pp1.salve.model.loja.Loja;

public interface ReviewRestauranteRepository extends JpaRepository<ReviewRestaurante, Long> {
    Page<ReviewRestaurante> findByLoja(Loja loja, Pageable pageable);
    
    boolean existsByLojaAndCriadoPorId(Loja loja, String id);
} 
