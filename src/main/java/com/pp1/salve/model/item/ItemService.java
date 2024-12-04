package com.pp1.salve.model.item;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.pp1.salve.minio.MinIOInterfacing;

import jakarta.transaction.Transactional;

@Service
public class ItemService {
    @Autowired
    private MinIOInterfacing minIOInterfacing;
    @Autowired
    private ItemRepository repository;

    public List<Item> findAll() {
        return repository.findAll();
    }

    public Item findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Item não encontrado"));
    }

    @Transactional
    public Item save(Item item,MultipartFile file) throws Exception {
        Item i = repository.save(item);
        minIOInterfacing.uploadFile("salve", i.getId().toString(), file);
        return i;
    }
    @Transactional
    public Item save(Item item) {
        Item i = repository.save(item);
        return i;
    }
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
