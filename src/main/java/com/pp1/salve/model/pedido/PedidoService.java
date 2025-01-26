package com.pp1.salve.model.pedido;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import com.pp1.salve.api.pedido.PedidoRequest;
import com.pp1.salve.exceptions.PedidoException;
import com.pp1.salve.model.endereco.EnderecoRepository;
import com.pp1.salve.model.item.Item;
import com.pp1.salve.model.item.ItemPedidoRepository;
import com.pp1.salve.model.item.ItemService;
import com.pp1.salve.model.loja.Loja;
import com.pp1.salve.model.loja.LojaService;
import com.pp1.salve.model.pedido.Pedido.Status;
import com.pp1.salve.model.pedido.itemDoPedido.ItemPedido;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PedidoService {
    private final PedidoRepository repository;
    private final EnderecoRepository enderecoSerice;
    private final LojaService lojaService;
    private final ItemService itemService;
    private final ItemPedidoRepository itemPedidoRepository;

    @Transactional(readOnly = true)
    public Page<Pedido> getMeusPedidos(Authentication authentication,Pageable pageable) throws Exception {
        Page<Pedido> pedido = repository.findByCriadoPorId(authentication.getName(),pageable);
      
        /*   isso serve para por as imagens dos itens no pedido mas no front não ta usando, pelo menos no front mobile
        for (Pedido p : pedido) {
            for (ItemPedido i : p.getItens()) {
                i.setItem(itemService.monta(i.getItem()));
            }
        } */
        return pedido;
    }

    @Transactional(readOnly = true)
    public Page<Pedido> getPedidosDaMinhaLoja(Authentication authentication,Pageable pageable) throws Exception {
        Loja loja = lojaService.findMyLoja(authentication);
        if (loja != null) {
            Page<Pedido> pedido = repository.findByLoja(loja,pageable);
            for (Pedido p : pedido) {
                for (ItemPedido i : p.getItens()) {
                    i.setItem(itemService.monta(i.getItem()));
                }
            }
            return pedido;
        }
        throw new EntityNotFoundException("Loja não encontrada ao procurar pedidos, você tem uma loja mesmo?");
    }
    public Page<Pedido> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Pedido findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado"));
    }

    @Transactional(rollbackFor = Exception.class)
    public Pedido save(PedidoRequest pedido) {
        Loja loja = lojaService.findById(pedido.getLojaId());
        double valorTotal = 0;
        List<Long> itemIds = pedido.getItens().stream()
                .map(item -> item.getId())
                .collect(Collectors.toList());

        if (!itemService.isSameStore(loja.getId(), itemIds)) {
            throw new PedidoException("Pedido com itens de lojas diferentes, por favor, faça pedidos separados");
        }
        //TODO: Implementar formas de pagamento e notificar loja

        Pedido ped = Pedido.builder()
                .enderecoEntrega(enderecoSerice.findById(pedido.getEnderecoEntregaId()).orElseThrow(() -> new EntityNotFoundException("Endereço não encontrado")))
                .valorTotal(valorTotal)
                .taxaEntrega(pedido.getTaxaEntrega())
                .formaPagamento("AINDA NÃO IMPLEMENTADO")
                .status(Status.PENDENTE)
                .loja(loja)
                .build();
        ped = repository.save(ped);
        for (ItemPedido p : pedido.getItens()) {
            Item item = itemService.findOne(p.getItem().getId());
            p.setValorUnitario(item.getValor());
            p.setItem(item);
            p.setPedido(ped);
            valorTotal += p.getValorUnitario() * p.getQuantidade();
            itemPedidoRepository.save(p);
        }
        ped.setValorTotal(valorTotal+ped.getTaxaEntrega());
        ped.setItens(pedido.getItens());
        return repository.save(ped);
    }

    @Transactional(rollbackFor = Exception.class)
    public Pedido updateStatus(Long id, Status status, Authentication authentication) {
        //TODO: implementar notificação para cliente e loja
        Pedido pedido = findById(id);
        pedido.setStatus(status);
        if (pedido.getLoja().getCriadoPor().getId().equals(authentication.getName())
                || pedido.getTrajetoriaEntregador().getEntregador().getId().equals(authentication.getName())) {
            return repository.save(pedido);
        } else {
            throw new PedidoException("Pedido você não pode alterar este pedido");
        }
    }
    @Transactional(rollbackFor = Exception.class)
    public Pedido updateStatus(Long id, String senha, Authentication authentication) {
        //TODO: implementar notificação para entregador, loja e cliente
        Pedido pedido = findById(id);
        if (pedido.getTrajetoriaEntregador().getEntregador().getId().equals(authentication.getName())
                && pedido.getCriadoPor().getPhone().contains(senha)) {
            pedido.setStatus(Status.ENTREGUE);
            return repository.save(pedido);
        } else {
            throw new PedidoException("Pedido você não pode alterar este pedido");
        }
    }
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
