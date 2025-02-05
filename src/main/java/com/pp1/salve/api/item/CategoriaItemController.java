package com.pp1.salve.api.item;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pp1.salve.model.item.CategoriaItem;
import com.pp1.salve.model.item.CategoriaItemService;
import com.pp1.salve.model.item.ResponseCategoriaEnMass;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/categoria-item")
@CrossOrigin
@Tag(name = "Categoria dos Itens", description = "pontos de interação com a Categoria de itens")
public class CategoriaItemController {

    @Autowired
    private CategoriaItemService service;

    @GetMapping
    public List<CategoriaItem> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaItem> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PreAuthorize("hasRole('admin')")
    @PostMapping("mass")
    public ResponseCategoriaEnMass postMass(@RequestBody List<String> entity) {
        
        return service.saveAll(entity);
    }
    @PreAuthorize("hasRole('admin')")
    @PostMapping
    public ResponseEntity<CategoriaItem> save(@RequestBody CategoriaItemRequest request) {
        CategoriaItem categoriaItem = service.save(request.build());
        return new ResponseEntity<CategoriaItem>(categoriaItem, HttpStatus.CREATED);
    }
    @PreAuthorize("hasRole('admin')")
    @PutMapping("/{id}")
    public ResponseEntity<CategoriaItem> update(@PathVariable Long id, @RequestBody CategoriaItem categoriaItem) {
        categoriaItem.setId(id);
        return ResponseEntity.ok(service.save(categoriaItem));
    }
    @PreAuthorize("hasRole('admin')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}