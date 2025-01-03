package com.pp1.salve.model.usuario;

import java.util.List;

import org.hibernate.validator.internal.util.stereotypes.Lazy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.pp1.salve.config.securtity.JwtService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;

@Service
public class UsuarioService implements UserDetailsService {
    @Autowired
    private JwtService jwtService;

    @Autowired
    private UsuarioRepository repository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    @Lazy
    private AuthenticationManager authenticationManager;

    
    public Usuario authenticate(String username, String password) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));

        return repository.findByUsername(username).orElseThrow();
    }

    @Transactional
    public Usuario findByUsername(String username) {

        return repository.findByUsername(username).get();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return repository.findByUsername(username).get();
    }

    @Transactional
    public Usuario save(Usuario user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setHabilitado(Boolean.TRUE);
        return repository.save(user);
    }

    public Usuario obterUsuarioLogado(HttpServletRequest request) {

        Usuario usuarioLogado = null;
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null) {

            String jwt = authHeader.substring(7);
            String userEmail = jwtService.extractUsername(jwt);
            usuarioLogado = findByUsername(userEmail);
            return usuarioLogado;
        }

        return usuarioLogado;
    }
    public List<Usuario> findAll() {
        return repository.findAll();
    }
    public Usuario findById(Long id) {
        return repository.findById(id).get();
    }
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
