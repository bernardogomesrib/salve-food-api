package com.pp1.salve.api.segmento;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pp1.salve.model.loja.SegmentoLoja;
import com.pp1.salve.model.loja.SegmentoLojaService;

@RestController
@RequestMapping("/api/segmento")
public class SegmentoController {
    @Autowired
    private SegmentoLojaService service;

    public SegmentoLoja save(SegmentoLojaPost segmentoLoja) {
        return service.save(segmentoLoja.build());
    }
    public SegmentoLoja findById(Long id) {
        return service.findById(id);
    }
    public void deleteById(Long id) {
        service.deleteById(id);
    }

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
