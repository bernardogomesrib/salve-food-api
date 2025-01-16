package com.pp1.salve.model.loja;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface SegmentoLojaRepository extends JpaRepository<SegmentoLoja, Long> {
    @Query("SELECT s FROM SegmentoLoja s WHERE s.nome.toLower() = ?1")
    List<SegmentoLoja> findByNome(String nome);

}
