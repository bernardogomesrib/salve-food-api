package com.pp1.salve.model.item;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ItemRepository extends JpaRepository<Item, Long> {
    @Query("SELECT i FROM Item i WHERE i.loja.id = ?1")
    Page<Item> findByLojaId(Long id, Pageable pageable);

    @Query("SELECT i FROM Item i WHERE i.loja.id = ?1")
    List<Item> findByLojaId(Long id);
    @Query("SELECT COUNT(i) > 0 FROM Item i WHERE i.loja.id = ?1 AND i.id IN ?2")
    boolean existsItemNotBelongingToLoja(Long lojaId, List<Long> itemIds);
    
  
}
