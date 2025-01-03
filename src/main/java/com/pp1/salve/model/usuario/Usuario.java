package com.pp1.salve.model.usuario;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pp1.salve.model.entidadeBase.BusinessEntity;
import com.pp1.salve.model.loja.Loja;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Usuario")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Usuario extends BusinessEntity implements UserDetails {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 100)
  private String nome;
  @Column(nullable = false, length = 100)
  private String username;
  @Column(nullable = false, length = 100)
  private String password;

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

  @JsonIgnore
  @ElementCollection(fetch = FetchType.EAGER)
  @Builder.Default
  private List<Role> roles = new ArrayList<>();

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return roles;
  }

  @Override
  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
