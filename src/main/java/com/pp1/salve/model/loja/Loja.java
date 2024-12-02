package com.pp1.salve.model.loja;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Entity
@Table(name = "Loja")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Loja {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(nullable = false, length = 45)
  private String nome;

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

  @Column(nullable = false, length = 45)
  private String longitude;

  @Column(nullable = false, length = 45)
  private String latitude;
}
