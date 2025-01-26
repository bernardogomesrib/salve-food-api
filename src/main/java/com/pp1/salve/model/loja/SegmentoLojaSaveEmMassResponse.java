package com.pp1.salve.model.loja;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SegmentoLojaSaveEmMassResponse {
    private List<SegmentoLoja> segmentoLojas = new ArrayList<>();
    private List<String> errors = new ArrayList<>();
    public int sucesso;
    public int falha;
    public int getSucesso() {
        return segmentoLojas.size();
    }
    public int getFalha() {
        return errors.size();
    }
}
