package com.pp1.salve.model.pedido;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.payment.PaymentCreateRequest;
import com.mercadopago.client.payment.PaymentPayerRequest;
import com.mercadopago.resources.payment.Payment;
import com.pp1.salve.api.pedido.PedidoRequest;
import com.pp1.salve.exceptions.UnauthorizedAccessException;
import com.pp1.salve.model.endereco.EnderecoRepository;
import com.pp1.salve.model.entregador.Entregador;
import com.pp1.salve.model.entregador.EntregadorService;
import com.pp1.salve.model.entregador.TrajetoriaEntregador;
import com.pp1.salve.model.entregador.TrajetoriaEntregadorRepository;
import com.pp1.salve.model.item.Item;
import com.pp1.salve.model.item.ItemService;
import com.pp1.salve.model.loja.Loja;
import com.pp1.salve.model.loja.LojaService;
import com.pp1.salve.model.pedido.Pedido.Status;
import com.pp1.salve.model.pedido.itemDoPedido.ItemPedido;
import com.pp1.salve.model.pedido.itemDoPedido.ItemPedidoFront;
import com.pp1.salve.model.pedido.itemDoPedido.ItemPedidoRepository;
import com.pp1.salve.model.usuario.Usuario;
import com.pp1.salve.model.usuario.UsuarioService;
import com.stripe.StripeClient;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PedidoService {
    private final UsuarioService usuarioService;
    private final EntregadorService entregadorService;
    private final TrajetoriaEntregadorRepository trajetoriaEntregadorRepository;
    private final PedidoRepository repository;
    private final EnderecoRepository enderecoSerice;
    private final LojaService lojaService;
    private final ItemService itemService;
    private final ItemPedidoRepository itemPedidoRepository;
    private final PaymentClient mercadoPagoClient;
    private final StripeClient stripeClient;
    @Value("${boolean.habilitado-pagamentos}")
    private Boolean isHabilitadoPagamento;

    @Transactional(readOnly = true)
    public Page<Pedido> getMeusPedidos(Authentication authentication, Pageable pageable) throws Exception {
        Page<Pedido> pedido = repository.findByCriadoPorId(authentication.getName(), pageable);

        /*
         * isso serve para por as imagens dos itens no pedido mas no front não ta
         * usando, pelo menos no front mobile
         * for (Pedido p : pedido) {
         * for (ItemPedido i : p.getItens()) {
         * i.setItem(itemService.monta(i.getItem()));
         * }
         * }
         */
        return pedido;
    }

    @Transactional(readOnly = true)
    public Page<Pedido> getPedidosDaMinhaLoja(Authentication authentication, Pageable pageable) throws Exception {
        Loja loja = lojaService.findMyLoja(authentication);
        if (loja != null) {
            Page<Pedido> pedido = repository.findByLoja(loja, pageable);
            for (Pedido p : pedido) {
                for (ItemPedido i : p.getItens()) {
                    i.setItem(itemService.monta(i.getItem()));
                }
                p.setSenha(null);
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
    public PedidoResposta save(PedidoRequest pedido, Authentication authentication) throws Exception {
        Loja loja = lojaService.findById(pedido.getLojaId());
        double valorTotal = 0;
        List<ItemPedidoFront> itensDoFront = pedido.getItens();

        List<Long> itemIds = itensDoFront.stream().map(ItemPedidoFront::getProduct).map(Item::getId)
                .collect(Collectors.toList());

        List<ItemPedido> itensParaSalvar = itensDoFront.stream().map(ItemPedidoFront::toItemPedido)
                .collect(Collectors.toList());
        if (!itemService.isSameStore(loja.getId(), itemIds)) {
            throw new UnauthorizedAccessException(
                    "Pedido com itens de lojas diferentes, por favor, faça pedidos separados");
        }
        // TODO: terminar de implementar formas de pagamento e notificar loja
        Usuario usuario = usuarioService.findUsuario(authentication);
        final String formaPagamento = pedido.getFormaPagamento();

        Pedido ped = Pedido.builder()
                .enderecoEntrega(enderecoSerice.findById(pedido.getEnderecoEntregaId())
                        .orElseThrow(() -> new EntityNotFoundException("Endereço não encontrado")))
                .valorTotal(valorTotal)
                .taxaEntrega(pedido.getTaxaEntrega())
                .formaPagamento(formaPagamento)
                .status(Status.PENDENTE)
                .loja(loja)
                .build();
        ped = repository.save(ped);
        for (ItemPedido p : itensParaSalvar) {
            Item item = itemService.findOne(p.getItem().getId());
            p.setValorUnitario(item.getValor());
            p.setItem(item);
            p.setPedido(ped);
            valorTotal += p.getValorUnitario() * p.getQuantidade();
            itemPedidoRepository.save(p);
        }
        ped.setValorTotal(valorTotal + ped.getTaxaEntrega());
        ped.setItens(itensParaSalvar);
        ped.setSenha(generateSenha());
        PedidoResposta pedidoResposta = PedidoResposta.builder().pedido(repository.save(ped)).build();
        if (isHabilitadoPagamento) {
            if (formaPagamento.equals("MERCADO_PAGO_PIX")) {
                PaymentCreateRequest createRequest = PaymentCreateRequest.builder()
                        .transactionAmount(BigDecimal.valueOf(ped.getValorTotal()))
                        .paymentMethodId("PIX")
                        .description(loja.getNome() + " - pedido de id - " + ped.getId())
                        .payer(PaymentPayerRequest.builder().email(usuario.getEmail()).firstName(usuario.getFirstName())
                                .lastName(usuario.getLastName()).build())
                        .build();
                Payment payment = mercadoPagoClient.create(createRequest);
                pedidoResposta.setPagamento(payment);

            } else if (formaPagamento.equals("STRIPE_CARD")) {

                PaymentIntent resp = stripeClient.paymentIntents().create(PaymentIntentCreateParams.builder()
                        .setAmount((long) (ped.getValorTotal() * 100))
                        .setCurrency("brl")
                        .addPaymentMethodType("card")
                        .setCustomer(usuario.getEmail())
                        .setDescription(loja.getNome() + " - pedido de id - " + ped.getId())
                        .build());
                pedidoResposta.setPagamento(resp);
            }
        }

        return pedidoResposta;
    }

    @Transactional(rollbackFor = Exception.class)
    public Pedido updateStatus(Long id, Status status, Authentication authentication) {
        // TODO: implementar notificação para cliente e loja
        Pedido pedido = findById(id);
        pedido.setStatus(status);
        if (pedido.getLoja().getCriadoPor().getId().equals(authentication.getName())) {
            return repository.save(pedido);
        } else {
            throw new UnauthorizedAccessException("Pedido você não pode alterar este pedido");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public Pedido updateStatus(Long id, String senha, Long entregadorId, Authentication authentication) {
        // TODO: implementar notificação para entregador, loja e cliente

        Pedido pedido = findById(id);
        if (pedido.getTrajetoriaEntregador().getEntregador().getId().equals(entregadorId)
                && pedido.getLoja().getCriadoPor().getId().equals(authentication.getName())) {
            if (pedido.getSenha().equals(senha)) {
                pedido.setStatus(Status.ENTREGUE);
                return repository.save(pedido);
            } else {
                throw new UnauthorizedAccessException("Senha incorreta para o pedido");
            }
        } else {
            throw new UnauthorizedAccessException("Pedido você não pode alterar este pedido");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    public Pedido setEntregador(Long id, Long entregadorId, Authentication authentication) {

        Pedido pedido = findById(id);

        if (pedido.getLoja().getCriadoPor().getId().equals(authentication.getName())) {

            Entregador entregador = entregadorService.findMeuEntregador(entregadorId, pedido.getLoja());
            TrajetoriaEntregador trajetoria = TrajetoriaEntregador.builder()
                    .entregador(entregador)
                    .pedido(pedido)
                    .build();
            trajetoria = trajetoriaEntregadorRepository.save(trajetoria);
            pedido.setTrajetoriaEntregador(trajetoria);
            return repository.save(pedido);
        } else {
            throw new UnauthorizedAccessException("Pedido você não pode alterar este pedido");
        }
    }

    public String generateSenha() {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(5);
        for (int i = 0; i < 5; i++) {
            sb.append((char) ('A' + random.nextInt(26)));
        }
        return sb.toString();
    }

}
