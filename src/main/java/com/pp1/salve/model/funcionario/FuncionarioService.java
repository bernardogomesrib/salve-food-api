package com.pp1.salve.model.funcionario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FuncionarioService {

    @Autowired
    private FuncionarioRepository repository;

    public List<Funcionario> findAll() {
        return repository.findAll();
    }

    public Funcionario findById(Integer id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Funcionário não encontrado"));
    }

    public Funcionario save(Funcionario funcionario) {
        return repository.save(funcionario);
    }

    public void deleteById(Integer id) {
        repository.deleteById(id);
    }
}
