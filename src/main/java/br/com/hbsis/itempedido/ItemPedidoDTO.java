package br.com.hbsis.itempedido;

public class ItemPedidoDTO {
    private Long id;
    private Long idPedido;
    private Long idProduto;
    private int quantidade;

    public ItemPedidoDTO() {
    }


    public ItemPedidoDTO(Long id, Long idPedido, Long idProduto, int quantidade) {
        this.id = id;
        this.idPedido = idPedido;
        this.idProduto = idProduto;
        this.quantidade = quantidade;
    }

    public static ItemPedidoDTO of(ItemPedido itemPedido){
        return new ItemPedidoDTO(
                itemPedido.getId(),
                itemPedido.getPedido().getId(),
                itemPedido.getProduto().getId(),
                itemPedido.getQuantidade()
        );
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(Long idPedido) {
        this.idPedido = idPedido;
    }

    public Long getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(Long idProduto) {
        this.idProduto = idProduto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }
}
