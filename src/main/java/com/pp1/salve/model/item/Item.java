package com.pp1.salve.model.item;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pp1.salve.model.baseModel.AuditEntityItem;
import com.pp1.salve.model.loja.Loja;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Item")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Item extends AuditEntityItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, length = 45)
    private String nome;
    @Column(nullable = false, length = 500)
    private String descricao;
    @Column(nullable = false)
    private Double valor;
    
    @JsonIgnore
    @ManyToOne
    @JoinColumn(nullable = false)
    private Loja loja;

    @ManyToOne
    @JoinColumn(nullable = false)
    private CategoriaItem categoriaItem;
    @Transient
    private String itemImage;

    @Column
    @Builder.Default
    private Boolean disponivel = true;


    @JsonIgnore
    @Builder.Default
    @Column(nullable = false)
    private boolean ativo = true;

    public Item(Long id) {
        this.id = id;
    }
}
