package com.pp1.salve.model.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService {

    @Autowired
    private ItemRepository repository;

    public List<Item> findAll() {
        return repository.findAll();
    }

    public Item findById(Integer id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Item não encontrado"));
    }

    public Item save(Item item) {
        return repository.save(item);
    }

    public void deleteById(Integer id) {
        repository.deleteById(id);
    }
}
