package br.com.hbsis.itempedido;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ItemPedidoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ItemPedidoService.class);
    private final IItemPedidoRepository iItemPedidoRepository;

    public ItemPedidoService(IItemPedidoRepository iItemPedidoRepository) {
        this.iItemPedidoRepository = iItemPedidoRepository;
    }

    public Set<ItemPedido> saveAll(Set<ItemPedido> itensPedido){
        LOGGER.info("Salvando itens do pedido");
        List<ItemPedido> itens = this.iItemPedidoRepository.saveAll(itensPedido);
        itensPedido.addAll(itens);
        return  itensPedido;
    }
    public ItemPedidoDTO findById(Long id) {
        Optional<ItemPedido> itempedidos = this.iItemPedidoRepository.findById(id);

        if (itempedidos.isPresent()) {
            return ItemPedidoDTO.of(itempedidos.get());
        }

        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }
}
