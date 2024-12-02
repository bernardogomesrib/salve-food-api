package com.pp1.salve.model.item;

import com.pp1.salve.model.loja.Loja;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Entity
@Table(name = "Item")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Item {

@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 45)
    private String nome;

    @Column(nullable = false)
    private Double valor;

    @Column(nullable = false)
    private Boolean alteravel;

    @ManyToOne
    @JoinColumn(name = "loja_id", nullable = false)
    private Loja loja;

    @ManyToOne
    @JoinColumn(name = "categoria_item_id", nullable = false)
    private CategoriaItem categoriaItem;
}
