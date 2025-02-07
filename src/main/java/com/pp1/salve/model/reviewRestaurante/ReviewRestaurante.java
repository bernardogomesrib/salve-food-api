package com.pp1.salve.model.reviewRestaurante;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.pp1.salve.model.baseModel.AuditEntityReview;
import com.pp1.salve.model.loja.Loja;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewRestaurante extends AuditEntityReview {

    @ManyToOne
    @JsonBackReference
    private Loja loja;
    @Column(nullable = false)
    private double nota;
    @Column(length = 500, nullable = true)
    private String comentario;
    @Transient
    private String imagem;
}