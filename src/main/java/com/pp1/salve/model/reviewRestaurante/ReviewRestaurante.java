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
    private double nota;
    @Column(nullable = false, length = 500)
    private String comentario;
    @Transient
    private String imagem;
}
