package com.pp1.salve.model.item;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
public class CategoriaItemService {

    @Autowired
    private CategoriaItemRepository repository;

    public List<CategoriaItem> findAll() {
        return repository.findAll();
    }

    public CategoriaItem findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("CategoriaItem não encontrada"));
    }

    @Transactional
    public CategoriaItem save(CategoriaItem categoriaItem) {
        return repository.save(categoriaItem);
    }
    
    @Transactional
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
