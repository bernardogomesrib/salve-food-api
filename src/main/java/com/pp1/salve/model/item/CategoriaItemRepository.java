package com.pp1.salve.model.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CategoriaItemRepository extends JpaRepository<CategoriaItem, Long> {
    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM CategoriaItem c WHERE LOWER(c.nome) = LOWER(?1)")
    boolean existsByNome(String nome);
}
