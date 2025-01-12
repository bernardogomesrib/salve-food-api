package com.pp1.salve.api.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.pp1.salve.model.item.Item;
import com.pp1.salve.model.item.ItemService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/item")
@CrossOrigin
@Tag(name = "Item", description = "pontos de interação com um item Item")
public class ItemController {

    @Autowired
    private ItemService service;

    @GetMapping
    public Page<Item> getAll(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) throws Exception {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return service.findAll(pageable);
    }

    @GetMapping("/{id}")
    public Item getById(@PathVariable Long id) throws Exception {
        return service.findById(id);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Item create(@RequestBody @Valid ItemRequest item) throws Exception {
        return service.save(item.build(), item.getFile());
    }

    @PutMapping("/{id}")
    public Item update(@PathVariable Long id, @RequestBody Item item) {
        item.setId(id);
        return service.save(item);
    }
    @PutMapping("file/{id}")
    public Item editarArquivo(@PathVariable Long id, @RequestBody MultipartFile file) throws Exception {
        return service.editarArquivo(id, file);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
