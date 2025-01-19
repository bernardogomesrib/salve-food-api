package com.pp1.salve.model.endereco;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.pp1.salve.exceptions.ResourceNotFoundException;
import com.pp1.salve.exceptions.UnauthorizedAccessException;

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
    public List<Endereco> findByUsuario(Authentication authentication) {
        return repository.findByUsuarioId(authentication.getName());
    }
    public Endereco update(Endereco endereco,Authentication authentication) {
        Endereco enderecoParaAtualizar = findById(endereco.getId());
        if(!enderecoParaAtualizar.getUsuario().getId().equals(authentication.getName())) {
            throw new UnauthorizedAccessException("Você não tem autoridade de modificar este endereço.");
        }
        enderecoParaAtualizar.setLatitude(endereco.getLatitude());
        enderecoParaAtualizar.setLongitude(endereco.getLongitude());
        enderecoParaAtualizar.setApelido(endereco.getApelido());
        enderecoParaAtualizar.setBairro(endereco.getBairro());
        enderecoParaAtualizar.setCep(endereco.getCep());
        enderecoParaAtualizar.setCidade(endereco.getCidade());
        enderecoParaAtualizar.setComplemento(endereco.getComplemento());
        enderecoParaAtualizar.setEstado(endereco.getEstado());
        enderecoParaAtualizar.setNumero(endereco.getNumero());
        enderecoParaAtualizar.setRua(endereco.getRua());
        return save(enderecoParaAtualizar);

    }
}
