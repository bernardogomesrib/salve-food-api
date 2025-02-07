package com.pp1.salve.model.loja;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LojaRepository extends JpaRepository<Loja, Long> {
    @Query("SELECT l FROM Loja l WHERE l.criadoPor.id = :criadoPor")
    Loja findByCriadoPorId(String criadoPor);

    @Query("SELECT l FROM Loja l WHERE l.segmentoLoja.id = :id ")
    Page<Loja> findBySegmentoLojaId(Long id, Pageable pageable);
    
    @Query("SELECT l FROM Loja l WHERE l.segmentoLoja.id = :id")
    Page<Loja> findBySegmentoLojaId(@Param("id") Long id,  @Param("lat") double lat, @Param("longi") double longi,Pageable pageable);

    @Query("SELECT l FROM Loja l WHERE l.segmentoLoja.id != 0")
    Page<Loja> findAllByPosition(@Param("lat") double lat, @Param("longi") double longi,Pageable pageable);
}