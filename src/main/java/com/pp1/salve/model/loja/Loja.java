package com.pp1.salve.model.loja;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.pp1.salve.model.baseModel.AuditEntityLoja;
import com.pp1.salve.model.entregador.Entregador;
import com.pp1.salve.model.reviewRestaurante.ReviewRestaurante;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Loja")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Loja extends AuditEntityLoja {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, length = 45)
    private String nome;

    @Column(length = 500)
    private String descricao;

    @Column(nullable = false, length = 45)
    private String rua;

    @Column(nullable = false, length = 45)
    private String numero;

    @Column(nullable = false, length = 45)
    private String bairro;

    @Column(nullable = false, length = 45)
    private String cidade;

    @Column(nullable = false, length = 45)
    private String estado;

    @ManyToOne
    @JoinColumn(name = "segmento_loja_id", nullable = false)
    private SegmentoLoja segmentoLoja;

    @Column(nullable = false, precision = 10)
    private double longitude;

    @Column(nullable = false, precision = 10)
    private double latitude;
    @Transient
    private String image;

    @OneToMany
    @JsonManagedReference
    private List<ReviewRestaurante> reviews;

    @OneToMany(mappedBy = "loja", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Entregador> entregadores;

    @Transient
    private Double rating;
    @Transient
    private Double deliveryTime;

    public Double getRating() {
        if (reviews != null && !reviews.isEmpty()) {
            double rating = 0;
            for (ReviewRestaurante reviewRestaurante : reviews) {
                rating += reviewRestaurante.getNota();
            }
            this.rating = rating / reviews.size();
            return this.rating;
        } else {
            return null;
        }
    }

}
