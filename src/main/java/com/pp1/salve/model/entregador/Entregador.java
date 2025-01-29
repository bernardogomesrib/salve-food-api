package com.pp1.salve.model.entregador;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.pp1.salve.model.baseModel.AuditEntity;
import com.pp1.salve.model.loja.Loja;
import com.pp1.salve.model.usuario.Usuario;

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
@Table(name = "Entregador")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Entregador extends AuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    @Transient
    private String image;

    @ManyToOne
    @JoinColumn(name = "loja_id", nullable = false)
    @JsonBackReference
    private Loja loja;

    @ManyToOne
    private Usuario usuario;  

}