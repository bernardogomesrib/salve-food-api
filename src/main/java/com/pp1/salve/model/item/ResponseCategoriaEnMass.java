package com.pp1.salve.model.item;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class ResponseCategoriaEnMass {
    private List<CategoriaItem> categoriaItens = new ArrayList<>();
    private List<String> errors = new ArrayList<>();;
    public int sucesso;
    public int falha;
    public int getSucesso(){
        return categoriaItens.size();
    }
    public int getFalha(){
        return errors.size();
    }
}
