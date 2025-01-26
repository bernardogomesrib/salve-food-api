package com.pp1.salve.model.loja;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.pp1.salve.exceptions.NoDuplicatedEntityException;
import com.pp1.salve.exceptions.ResourceNotFoundException;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SegmentoLojaService {
    @Autowired
    private SegmentoLojaRepository segmentoLojaRepository;

    @Transactional
    public SegmentoLojaSaveEmMassResponse saveAll(List<String> entity) {
        List<SegmentoLoja> segmentoLojas = new ArrayList<>();
        for (String nome : entity) {
            SegmentoLoja segmentoLoja = new SegmentoLoja();
            segmentoLoja.setNome(nome);
            segmentoLojas.add(segmentoLoja);
        }
        SegmentoLojaSaveEmMassResponse response = new SegmentoLojaSaveEmMassResponse();
        for (SegmentoLoja segmentoLoja : segmentoLojas) {
            try {
                segmentoLoja = save(segmentoLoja);
                response.getSegmentoLojas().add(segmentoLoja);
            } catch (NoDuplicatedEntityException e) {
                response.getErrors().add(e.getMessage());
            }
        }

        return response;
    }

    @Transactional
    public SegmentoLojaSaveEmMassResponse updateAll(List<SegmentoLoja> entity) {
        SegmentoLojaSaveEmMassResponse response = new SegmentoLojaSaveEmMassResponse();
        for (SegmentoLoja segmentoLoja : entity) {
            try {
                segmentoLoja = update(segmentoLoja);
                response.getSegmentoLojas().add(segmentoLoja);
            } catch (NoDuplicatedEntityException e) {
                response.getErrors().add(e.getMessage());
            }
        }
        return response;
    }


    @Transactional
    public SegmentoLoja save(SegmentoLoja segmentoLoja) {
        if (segmentoLojaRepository.findByNome(segmentoLoja.getNome()).size() > 0) {
            throw new NoDuplicatedEntityException("Segmento de loja já cadastrado com nome " + segmentoLoja.getNome());
        }
        return segmentoLojaRepository.save(segmentoLoja);
    }

    @Transactional
    public SegmentoLoja update(SegmentoLoja segmentoLoja) {
        if(segmentoLojaRepository.existsById(segmentoLoja.getId())){
            return segmentoLojaRepository.save(segmentoLoja);
        }else{
            throw new ResourceNotFoundException("Segmento de loja não encontrado com id " + segmentoLoja.getId());
        }
    }

    public SegmentoLoja findById(Long id) {
        return segmentoLojaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Segmento de loja não encontrado com id " + id));
    }

    public void deleteById(Long id) {
        segmentoLojaRepository.deleteById(id);
    }

    public Page<SegmentoLoja> findAll(Pageable pageable) {
        return segmentoLojaRepository.findAll(pageable);
    }

    public List<SegmentoLoja> findAllUsed() {
        return segmentoLojaRepository.findAllUsedSegments();
    }
}
