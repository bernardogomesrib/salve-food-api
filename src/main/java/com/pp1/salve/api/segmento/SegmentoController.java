package com.pp1.salve.api.segmento;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pp1.salve.model.loja.SegmentoLoja;
import com.pp1.salve.model.loja.SegmentoLojaSaveEmMassResponse;
import com.pp1.salve.model.loja.SegmentoLojaService;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/api/segmento")
@Tag(name = "Segmento de Loja",description = "pontos de interação com um Segmento de Loja")
public class SegmentoController {
    @Autowired
    private SegmentoLojaService service;

    @PreAuthorize("hasRole('admin')")
    @PostMapping("mass")
    public SegmentoLojaSaveEmMassResponse postSegmentoEmMassa(@RequestBody List<String> entity) {
        return service.saveAll(entity);
    }
    
    @PostMapping
    public SegmentoLoja save(@RequestBody SegmentoLojaPost segmentoLoja) {
        return service.save(segmentoLoja.build());
    }
    @GetMapping("{id}")
    public SegmentoLoja findById(@PathVariable Long id) {
        return service.findById(id);
    }
    @DeleteMapping("{id}")
    public void deleteById(@PathVariable Long id) {
        service.deleteById(id);
    }
    @GetMapping
    public Iterable<SegmentoLoja> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return service.findAll(pageable);
    }
}
