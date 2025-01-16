package com.pp1.salve.api.item;

import java.util.List;

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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pp1.salve.model.item.Item;
import com.pp1.salve.model.item.ItemService;

import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(summary = "Busca items por id de loja")
    @GetMapping("/{id}")
    public List<Item> getByLojaId(@PathVariable Long id) throws Exception {
        return service.findByLojaId(id);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Item create(@ModelAttribute @Valid ItemRequestSave item) throws Exception {
        return service.save(item);
    }

    @PutMapping(value = "/{id}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Item update(@PathVariable Long id,@ModelAttribute @Valid ItemRequest item) throws Exception {
        return service.editarItem(id,item);
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) throws Exception {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
