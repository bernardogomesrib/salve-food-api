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

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@RestController
@RequestMapping("/api/pedidos")
@CrossOrigin
@Tag(name = "Pedidos", description = "pontos de interação com Pedidos")
public class PedidosController {

    @Autowired
    private PedidoService service;

    @GetMapping
    public Page<Pedido> getAll(@RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size)  {
        Pageable pageable = PageRequest.of(page, size,Sort.by("id").descending());
        return service.findAll(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pedido> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<Pedido> create(@RequestBody PedidoRequest pedido) {
        return ResponseEntity.ok(service.save(pedido));
    }

    @PreAuthorize("hasRole('dono_de_loja')")
    @PutMapping("/{id}/preparando")
    public ResponseEntity<Pedido> atualizarPedidoPreparando(@PathVariable Long id,Authentication authentication) {
        return ResponseEntity.ok(service.updateStatus(id, Status.PREPARANDO,authentication));
    }
    @PreAuthorize("hasRole('dono_de_loja')")
    @PutMapping("{id}/aguardando-entregador")
    public ResponseEntity<Pedido> atualizarPedidoAguardando(@PathVariable Long id,Authentication authentication) {
        return ResponseEntity.ok(service.updateStatus(id, Status.AGUARDANDO_ENTREGADOR,authentication));
    }
    @PreAuthorize("hasRole('dono_de_loja')")
    @PutMapping("{id}/cancelado")
    public ResponseEntity<Pedido> cancelado(@PathVariable Long id,Authentication authentication) {
        return ResponseEntity.ok(service.updateStatus(id, Status.CANCELADO,authentication));
    }

    @PreAuthorize("hasRole('entregador')")
    @PutMapping("/{id}/entregue")
    public ResponseEntity<Pedido> entregue(@PathVariable Long id, @RequestBody @Min(4) @Max(4)String senha,Authentication authentication) {
        return ResponseEntity.ok(service.updateStatus(id,senha,authentication));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
