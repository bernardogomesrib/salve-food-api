package com.pp1.salve.model.usuario;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity(name = "user_entity")
public class Usuario {
    @Id
    private String id;
    private String telefone;
}
