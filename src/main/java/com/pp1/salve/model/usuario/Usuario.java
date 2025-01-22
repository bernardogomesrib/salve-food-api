package com.pp1.salve.model.usuario;

import java.time.LocalDateTime;

import com.pp1.salve.model.loja.Loja;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity(name = "usuario")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Usuario {
    private final static int LAST_ACTIVE_INTERVAL = 5;
    @Id
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private LocalDateTime lastSeenAt;
    

    @OneToOne
    @JoinColumn(nullable = true)
    private Loja loja;
    @Transient
    public boolean isOnline() {
        return lastSeenAt != null && lastSeenAt.isAfter(LocalDateTime.now().minusMinutes(LAST_ACTIVE_INTERVAL));
    }
    
}
