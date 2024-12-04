package com.pp1.salve.model.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    @Transactional
    public Page<Item> findAll(Pageable pageable) throws Exception {
        Page<Item> items = repository.findAll(pageable);
        items.forEach(item -> {
            try {
                item.setItemImage(minIOInterfacing.getSingleUrl("salve", item.getId().toString()));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        return items;
    }
    
    @Transactional
    public Item findById(Long id) throws Exception {
       Item i = repository.findById(id).orElseThrow(() -> new RuntimeException("Item não encontrado"));
        i.setItemImage(minIOInterfacing.getSingleUrl("salve-items", i.getId().toString()));
       return i;
    }

    @Transactional
    public Item save(Item item,MultipartFile file) throws Exception {
        Item i = repository.save(item);
        minIOInterfacing.uploadFile("salve-items", i.getId().toString(), file);
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
