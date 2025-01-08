package com.pp1.salve.model.loja;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.pp1.salve.exceptions.ResourceNotFoundException;

@Service
public class SegmentoLojaService {
    @Autowired
    private SegmentoLojaRepository segmentoLojaRepository;
    public SegmentoLoja save(SegmentoLoja segmentoLoja) {
        return segmentoLojaRepository.save(segmentoLoja);
    }
    public SegmentoLoja findById(Long id) {
        return segmentoLojaRepository.findById(id).orElseThrow(() ->  new ResourceNotFoundException("Segmento de loja não encontrado com id " + id));
    }
    public void deleteById(Long id) {
        segmentoLojaRepository.deleteById(id);
    }
    public Iterable<SegmentoLoja> findAll(Pageable pageable) {
        return segmentoLojaRepository.findAll(pageable);
    }

}
