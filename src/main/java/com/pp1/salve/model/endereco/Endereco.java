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

    @Column(nullable = false, length = 45)
    private String rua;

    @Column(nullable = false, length = 45)
    private String numero;

    @Column(length = 255)
    private String complemento;

    @Column(nullable = false, length = 45)
    private String bairro;

    @Column(nullable = false, length = 45)
    private String cidade;

    @Column(nullable = false, length = 45)
    private String estado;
}
