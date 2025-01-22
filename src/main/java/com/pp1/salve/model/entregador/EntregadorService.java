package com.pp1.salve.model.entregador;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.pp1.salve.minio.MinIOInterfacing;
import com.pp1.salve.model.usuario.Usuario;
import com.pp1.salve.model.usuario.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EntregadorService {
    private static final String ENTREGADOR = "entregador";
    private static final String ENTREGADOR_IMAGE = "entregadorImage";

    private final MinIOInterfacing minIOInterfacing;
    private final EntregadorRepository repository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Entregador save(Entregador entregador, MultipartFile file, Authentication authentication) throws Exception {
        Usuario savedUsuario = usuarioRepository.save(entregador.getUsuario());
        entregador.setUsuario(savedUsuario);

        Entregador entregadorSave = repository.save(entregador);

        if (file != null && !file.isEmpty()) {
            String imagePath = minIOInterfacing.uploadFile(ENTREGADOR, entregadorSave.getId() + ENTREGADOR_IMAGE, file);
            entregadorSave.setImage(imagePath);
        }

        return repository.save(entregadorSave);
    }

    public List<Entregador> findByLoja(Long lojaId) throws Exception {
        if (!repository.existsByLojaId(lojaId)) {
            throw new Exception("Loja não encontrada com o ID fornecido: " + lojaId);
        }
    
        return repository.findByLojaId(lojaId);
    }

    public Entregador updateStatus(Long id, Boolean disponivel, Authentication authentication) throws Exception {
        Entregador entregador = repository.findById(id)
                .orElseThrow(() -> new Exception("Entregador não encontrado"));

        entregador.setDisponivel(disponivel);
        return repository.save(entregador);
    }

    public void delete(Long id, Authentication authentication) throws Exception {
        repository.findById(id)
                .orElseThrow(() -> new Exception("Entregador não encontrado"));
        repository.deleteById(id);
    }


}
