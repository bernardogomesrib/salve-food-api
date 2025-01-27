package com.pp1.salve.model.baseModel;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pp1.salve.model.usuario.Usuario;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AuditEntity extends BusinessEntity {

   @JsonIgnore
   @Version
   private Long versao;

   @JsonIgnore
   @CreatedDate
   @Column(nullable = false, updatable = false)
   private LocalDateTime dataCriacao;

   @JsonIgnore
   @LastModifiedDate
   @Column(insertable = false)
   private LocalDateTime dataUltimaModificacao;

   @JsonIgnore
   @CreatedBy
   @ManyToOne
   private Usuario criadoPor;

   @JsonIgnore
   @LastModifiedBy
   @ManyToOne
   private Usuario ultimaModificacaoPor;

}