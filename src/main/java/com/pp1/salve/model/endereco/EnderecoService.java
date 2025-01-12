package com.pp1.salve.model.endereco;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pp1.salve.exceptions.ResourceNotFoundException;

@Service
public class EnderecoService {

    @Autowired
    private EnderecoRepository repository;

    public List<Endereco> findAll() {
        return repository.findAll();
    }

    public Endereco findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Endereço não encontrado"));
    }

    public Endereco save(Endereco endereco) {
        return repository.save(endereco);
    }

    public void deleteById(Long id) {
        repository.delete(findById(id));
    }
}
