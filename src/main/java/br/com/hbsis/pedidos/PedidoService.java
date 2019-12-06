package br.com.hbsis.pedidos;

import br.com.hbsis.fornecedor.FornecedorService;
import br.com.hbsis.itempedido.ItemPedido;
import br.com.hbsis.itempedido.ItemPedidoDTO;
import br.com.hbsis.itempedido.ItemPedidoService;
import br.com.hbsis.produtos.Produto;
import br.com.hbsis.produtos.ProdutoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PedidoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PedidoService.class);

    private final IPedidoRepository iPedidoRepository;
    private final ProdutoService produtoService;
    private final ItemPedidoService itemPedidoService;

    public PedidoService(IPedidoRepository iPedidoRepository, ProdutoService produtoService, ItemPedidoService itemPedidoService) {
        this.iPedidoRepository = iPedidoRepository;
        this.produtoService = produtoService;
        this.itemPedidoService = itemPedidoService;
    }


    public PedidoDTO save(PedidoDTO pedidoDTO) {

        this.validate(pedidoDTO);
        LOGGER.info("Salvando Pedido");
        LOGGER.debug("Produto: {}", pedidoDTO);

        Set<ItemPedido> itemPedidoCompletos = this.parseToItemPedido(pedidoDTO.getItemPedidoDTO());

        double valorTotal = valorTotalPedido(itemPedidoCompletos);

        Pedido pedido = new Pedido(valorTotal);
        pedido.setItensPedido(itemPedidoCompletos);

        for (ItemPedido itemPedido : pedido.getItensPedido()) {
            itemPedido.setPedido(pedido);
        }

        pedido = this.iPedidoRepository.save(pedido);
        return PedidoDTO.of(pedido);
    }

    public double valorTotalPedido(Set<ItemPedido> itensPedido) {
        Double valorTotal = 0.0;
        for (ItemPedido ip : itensPedido) {
            Produto produtoDoPedido = produtoService.findProdutoById(ip.getProduto().getId());
            valorTotal += (produtoDoPedido.getPrecoProduto() * ip.getQuantidade());

        }
        return valorTotal;
    }


    public Set<ItemPedido> saveItensPedido(Set<ItemPedido> itensPedido, Pedido pedido) {
        itensPedido.forEach(ip -> ip.setPedido(pedido));
        return this.itemPedidoService.saveAll(itensPedido);
    }

    public Set<ItemPedido> parseToItemPedido(Set<ItemPedidoDTO> itemPedidosDTO) {
        Set<ItemPedido> itemPedidoSet = new HashSet<>();

        for (ItemPedidoDTO ipDTO : itemPedidosDTO) {
            ItemPedido itemPedido = new ItemPedido();
            itemPedido.setProduto(produtoService.findProdutoById(ipDTO.getIdProduto()));
            itemPedido.setQuantidade(ipDTO.getQuantidade());
            itemPedidoSet.add(itemPedido);
        }

        return itemPedidoSet;
    }

    private void validate(PedidoDTO pedidoDTO) {
        LOGGER.info("Validando Pedido");

        if (pedidoDTO == null) {
            throw new IllegalArgumentException("Pedido não deve ser nula");
        }

        if (pedidoDTO.getItemPedidoDTO().isEmpty()) {
            throw new IllegalArgumentException("Pedido tem que ter produtos");
        }

    }

    public PedidoDTO findById(Long id) {
        Optional<Pedido> pedidos = this.iPedidoRepository.findById(id);

        if (pedidos.isPresent()) {
            return PedidoDTO.of(pedidos.get());
        }

        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }


    public List<PedidoDTO> findAll() {

        List<Pedido> pedidos = iPedidoRepository.findAll();
        List<PedidoDTO> pedidosDTO = new ArrayList<>();
        pedidos.forEach(pedido -> pedidosDTO.add(PedidoDTO.of(pedido)));

        return pedidosDTO;
    }

    public PedidoDTO update(PedidoDTO pedidoDTO, Long id) {
        Optional<Pedido> pedidoOptional = this.iPedidoRepository.findById(id);


        if (pedidoOptional.isPresent()) {
            Pedido pedidoExistente = pedidoOptional.get();

            LOGGER.info("Atualizando pedido... id: [{}]", pedidoExistente.getId());
            LOGGER.debug("Payload: {}", pedidoExistente.toString());


            pedidoExistente.setItensPedido((this.parseToItemPedido(pedidoDTO.getItemPedidoDTO())));
            pedidoExistente.setValorTotal(pedidoDTO.getValorTotal());

            pedidoExistente = this.iPedidoRepository.save(pedidoExistente);

            return PedidoDTO.of(pedidoExistente);
        }
        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    public void delete(Long id) {
        LOGGER.info("Executando delete para pedido de ID: [{}]", id);

        this.iPedidoRepository.deleteById(id);
    }
}
