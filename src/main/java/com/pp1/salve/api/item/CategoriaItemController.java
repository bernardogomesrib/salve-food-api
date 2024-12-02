package com.pp1.salve.api.item;

import com.pp1.salve.model.item.CategoriaItem;
import com.pp1.salve.model.item.CategoriaItemService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categoria-item")
@CrossOrigin
public class CategoriaItemController {

    @Autowired
    private CategoriaItemService service;

    @GetMapping
    public List<CategoriaItem> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaItem> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<CategoriaItem> save(@RequestBody CategoriaItemRequest request) {
        CategoriaItem categoriaItem = service.save(request.build());
        return new ResponseEntity<CategoriaItem>(categoriaItem, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaItem> update(@PathVariable Integer id, @RequestBody CategoriaItem categoriaItem) {
        categoriaItem.setId(id);
        return ResponseEntity.ok(service.save(categoriaItem));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}