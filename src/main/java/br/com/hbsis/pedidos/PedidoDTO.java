package br.com.hbsis.pedidos;

import br.com.hbsis.itempedido.ItemPedido;
import br.com.hbsis.itempedido.ItemPedidoDTO;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


public class PedidoDTO {

    private Long id;
    private Set<ItemPedidoDTO> itemPedidoDTO;
    private String status;
    private String codigo;
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
        for (ItemPedido ip: pedido.getItensPedido()) {

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
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
