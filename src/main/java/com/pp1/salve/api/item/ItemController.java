package com.pp1.salve.api.item;

import com.pp1.salve.model.item.Item;
import com.pp1.salve.model.item.ItemService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/item")
@CrossOrigin
public class ItemController {

    @Autowired
    private ItemService service;

    @GetMapping
    public List<Item> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Item> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<Item> create(@RequestBody Item item) {
        return ResponseEntity.ok(service.save(item));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Item> update(@PathVariable Integer id, @RequestBody Item item) {
        item.setId(id);
        return ResponseEntity.ok(service.save(item));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
