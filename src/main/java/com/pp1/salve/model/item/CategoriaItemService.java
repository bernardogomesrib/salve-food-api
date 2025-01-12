package com.pp1.salve.model.item;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pp1.salve.exceptions.ResourceNotFoundException;

import jakarta.transaction.Transactional;

@Service
public class CategoriaItemService {

    @Autowired
    private CategoriaItemRepository repository;

    public List<CategoriaItem> findAll() {
        return repository.findAll();
    }

    public CategoriaItem findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Categoria de Item não encontrada com id: "+id));
    }

    @Transactional
    public CategoriaItem save(CategoriaItem categoriaItem) {
        return repository.save(categoriaItem);
    }
    
    @Transactional
    public void deleteById(Long id) {

        repository.delete(findById(id));
    }
}
