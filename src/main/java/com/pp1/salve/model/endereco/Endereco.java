package com.pp1.salve.model.endereco;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pp1.salve.model.usuario.Usuario;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Entity
@Table(name = "endereco")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Endereco {

@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "user_id", nullable = false)
    private Usuario usuario;
    @Column(nullable = true, length = 45)
    private String apelido;

    @Column(nullable = false, length = 255)
    private String rua;

    @Column(nullable = false, length = 255)
    private String numero;

    @Column(length = 255)
    private String complemento;

    @Column(nullable = false, length = 255)
    private String bairro;

    @Column(nullable = false, length = 255)
    private String cidade;

    @Column(nullable = false, length = 255)
    private String estado;

    @Column(nullable = false)
    private double latitude;

    @Column(nullable = false)
    private double longitude;
    @Column(nullable = true, length = 10)
    private String cep;
    
    @Builder.Default
    @Column(nullable = false)
    private boolean ativo = true;

    public void setUsuario(String id) {
        this.usuario = Usuario.builder().id(id).build();
    }
}
