package com.pp1.salve.model.usuario;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UsuarioRepository extends JpaRepository <Usuario, Long> {
    @Query("SELECT u FROM Usuario u WHERE u.email = :username")
    Optional<Usuario> findByUsername(String username);
}
