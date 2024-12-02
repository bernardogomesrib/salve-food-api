package com.pp1.salve.model.usuario;

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
@Table(name = "Usuario")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Usuario {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(nullable = false, length = 100)
  private String nome;

  @Column(nullable = false, unique = true, length = 100)
  private String email;

  @Column(unique = true, length = 45)
  private String cpf;

  @Column(length = 45)
  private String rg;

  @Column(length = 45)
  private String cnh;

  @Enumerated(EnumType.STRING)
  @Column(name = "tipo_cnh", length = 10)
  private TipoCnh tipoCnh;

  @ManyToOne
  @JoinColumn(name = "loja_id")
  private Loja loja;

  @Column(nullable = false)
  private Boolean entregador;

  public enum TipoCnh {
    A, B, C, D, E;
  }
}
