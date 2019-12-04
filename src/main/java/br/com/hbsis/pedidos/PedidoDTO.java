package br.com.hbsis.pedidos;

import br.com.hbsis.itempedido.ItemPedido;
import br.com.hbsis.itempedido.ItemPedidoDTO;

import java.util.HashSet;
import java.util.Set;


public class PedidoDTO {

    private Long id;
    private Set<ItemPedidoDTO> itemPedidoDTO;
    private Double valorTotal;

    public PedidoDTO() {
    }

    public PedidoDTO(Long id, Set<ItemPedidoDTO> itemPedidoDTO, Double valorTotal) {
        this.id = id;
        this.itemPedidoDTO = itemPedidoDTO;
        this.valorTotal = valorTotal;
    }

    public static PedidoDTO of (Pedido pedido){
        Set<ItemPedidoDTO> ipDTO = new HashSet<>();
        for (ItemPedido ip: pedido.getItemPedidoSet()) {

           ipDTO.add(ItemPedidoDTO.of(ip));
        }
       PedidoDTO pdto = new PedidoDTO(
                pedido.getId(),
                ipDTO,
                pedido.getValorTotal()
        );
        return  pdto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<ItemPedidoDTO> getItemPedidoDTO() {
        return itemPedidoDTO;
    }

    public void setItemPedidoDTO(Set<ItemPedidoDTO> itemPedidoDTO) {
        this.itemPedidoDTO = itemPedidoDTO;
    }

    public Double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(Double valorTotal) {
        this.valorTotal = valorTotal;
    }

    @Override
    public String toString() {
        return "PedidoDTO{" +
                "id=" + id +
                ", Valor Total ='" +valorTotal+ '\'' +
                ", Size ='" + itemPedidoDTO.size() + '\'' +
                '}';
    }
}
