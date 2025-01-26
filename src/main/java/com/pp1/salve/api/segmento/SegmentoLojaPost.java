package com.pp1.salve.api.segmento;

import com.pp1.salve.model.loja.SegmentoLoja;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SegmentoLojaPost {
    private String nome;
    public SegmentoLoja build() {
        return SegmentoLoja.builder()
            .nome(nome)
            .build();
    }
}
