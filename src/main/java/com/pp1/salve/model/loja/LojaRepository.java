package com.pp1.salve.model.loja;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface LojaRepository extends JpaRepository<Loja, Long> {
    @Query("SELECT l FROM Loja l WHERE l.criadoPor.id = :criadoPor")
    Loja findByCriadoPorId(String criadoPor);

    @Query("SELECT l FROM Loja l WHERE l.segmentoLoja.id = :id ORDER BY (6371 * acos(cos(radians(:lat)) * cos(radians(l.latitude)) * cos(radians(l.longitude) - radians(:longi)) + sin(radians(:lat)) * sin(radians(l.latitude))))")
    Page<Loja> findBySegmentoLojaId(Long id, Pageable pageable, double lat, double longi);

    @Query("SELECT l FROM Loja l WHERE l.segmentoLoja.id = :id ")
    Page<Loja> findBySegmentoLojaId(Long id, Pageable pageable);

    @Query("SELECT l FROM Loja l ORDER BY (6371 * acos(cos(radians(:lat)) * cos(radians(l.latitude)) * cos(radians(l.longitude) - radians(:longi)) + sin(radians(:lat)) * sin(radians(l.latitude))) )")
    Page<Loja> findAll(Pageable pageable, double lat, double longi);
    
}
