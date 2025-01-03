package com.pp1.salve.model.usuario;



import org.hibernate.annotations.SQLRestriction;
import org.springframework.security.core.GrantedAuthority;

import com.pp1.salve.model.entidadeBase.BusinessEntity;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@SQLRestriction("habilitado = true")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Role extends BusinessEntity implements GrantedAuthority {
  
   public static final String ROLE_CLIENTE = "CLIENTE";
   public static final String ROLE_FUNCIONARIO_ADMIN = "ROLE_FUNCIONARIO_ADMIN"; // READ, DELETE, WRITE, UPDATE.
   public static final String ROLE_FUNCIONARIO_USER = "ROLE_FUNCIONARIO_USER"; // READ, WRITE, UPDATE.
  
   private String nome;
  
   @Override
   public String getAuthority() {
       return this.nome;
   }
  
}