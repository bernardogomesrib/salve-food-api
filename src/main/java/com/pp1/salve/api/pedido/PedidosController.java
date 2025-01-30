package com.pp1.salve.api.pedido;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pp1.salve.model.pedido.Pedido;
import com.pp1.salve.model.pedido.PedidoService;
import com.pp1.salve.model.pedido.Pedido.Status;
import com.pp1.salve.model.pedido.PedidoResposta;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/pedidos")
@CrossOrigin
@Tag(name = "Pedidos", description = "pontos de interação com Pedidos")
public class PedidosController {

    @Autowired
    private PedidoService service;

    @Operation(summary = "Pega todos os pedidos", description = "Pega todos os pedidos organizando por id, disponivel apenas para administrador")
    @PreAuthorize("hasRole('admin')")
    @GetMapping
    public Page<Pedido> getAll(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return service.findAll(pageable);
    }

    @Operation(summary = "Pega um pedido pelo id", description = "não coloquei nem uma restrição")
    @GetMapping("/{id}")
    public ResponseEntity<Pedido> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @Operation(summary = "Cria um pedido para ser aceito ou negado por uma loja")
    @PostMapping
    public ResponseEntity<PedidoResposta> create(@RequestBody PedidoRequest pedido, Authentication authentication)
            throws Exception {
        return ResponseEntity.ok(service.save(pedido, authentication));
    }

    @Operation(summary = "Atualiza o status do pedido para aceito", description = "Atualiza o status do pedido para aceito, precisa de role de dono de loja")
    @PreAuthorize("hasRole('dono_de_loja')")
    @PutMapping("/{id}/preparando")
    public ResponseEntity<Pedido> atualizarPedidoPreparando(@PathVariable Long id, Authentication authentication) {
        return ResponseEntity.ok(service.updateStatus(id, Status.PREPARANDO, authentication));
    }

    @Operation(summary = "Atualiza o pedido para aguardando o entregador")
    @PreAuthorize("hasRole('dono_de_loja')")
    @PutMapping("{id}/aguardando-entregador")
    public ResponseEntity<Pedido> atualizarPedidoAguardando(@PathVariable Long id, Authentication authentication) {
        return ResponseEntity.ok(service.updateStatus(id, Status.AGUARDANDO_ENTREGADOR, authentication));
    }

    @Operation(summary = "Atualiza o pedido para cancelado")
    @PreAuthorize("hasRole('dono_de_loja')")
    @PutMapping("{id}/cancelado")
    public ResponseEntity<Pedido> cancelado(@PathVariable Long id, Authentication authentication) {
        return ResponseEntity.ok(service.updateStatus(id, Status.CANCELADO, authentication));
    }

    @Operation(summary = "Atualiza o pedido para entregue", description = "Atualiza o pedido para entregue, precisa da senha do pedido")
    @PreAuthorize("hasRole('dono_de_loja')")
    @PutMapping("/{id}/entregue")
    public ResponseEntity<Pedido> entregue(@PathVariable Long id,
            @RequestBody PedidoConclusaoRequest pedidoConclusaoRequest, Authentication authentication) {
        return ResponseEntity.ok(service.updateStatus(id, pedidoConclusaoRequest.getSenha(),
                pedidoConclusaoRequest.getIdEntregador(), authentication));
    }

    @Operation(summary = "Deletar pedido", description = "Deleta um pedido pelo id. precisa de role de admin")
    @PreAuthorize("hasRole('admin')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Obter meus pedidos", description = "Retorna uma lista paginada dos pedidos do usuário autenticado.")
    @GetMapping("meus")
    public ResponseEntity<?> getMeusPedidos(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size, Authentication authentication) throws Exception {
        return ResponseEntity.ok()
                .body(service.getMeusPedidos(authentication, PageRequest.of(page, size, Sort.by("id").descending())));
    }

    @Operation(summary = "Obter pedidos da minha loja", description = "Retorna uma lista paginada dos pedidos da minha loja.")
    @PreAuthorize("hasRole('dono_de_loja')")
    @GetMapping("/loja")
    public ResponseEntity<?> getMeusPedidos(Authentication authentication, @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) throws Exception {
        return ResponseEntity.ok().body(
                service.getPedidosDaMinhaLoja(authentication, PageRequest.of(page, size, Sort.by("id").descending())));
    }

    @Operation(summary = "define um entregador para um pedido", description = "define um entregador para um pedido com base no id do entregador e no id do pedido")
    @PreAuthorize("hasRole('dono_de_loja')")
    @PutMapping("/{id}/entregador/{entregadorId}")
    public ResponseEntity<Pedido> setEntregador(@PathVariable Long id, @PathVariable Long entregadorId,
            Authentication authentication) {
        return ResponseEntity.ok(service.setEntregador(id, entregadorId, authentication));
    }
}
