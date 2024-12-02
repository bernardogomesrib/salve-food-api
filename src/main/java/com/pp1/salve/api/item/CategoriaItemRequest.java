package com.pp1.salve.api.item;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.pp1.salve.model.item.CategoriaItem;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaItemRequest {

    private String nome;

    public CategoriaItem build() {
        return CategoriaItem.builder()
            .nome(nome)
            .build();
    }
}
