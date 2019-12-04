package br.com.hbsis.itempedido;

import br.com.hbsis.pedidos.Pedido;
import br.com.hbsis.produtos.Produto;

import javax.persistence.*;

@Entity
@Table(name = "item_pedido")
public class ItemPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pedido", referencedColumnName = "id")
    private Pedido pedido;

    @OneToOne
    @JoinColumn(name = "id_produto", referencedColumnName = "id")
    private Produto produto;

    @Column(name = "quantidade", nullable = false)
    private int quantidade;

    public ItemPedido() {
    }

    public ItemPedido(Pedido pedido, Produto produto, int quantidade) {
        this.pedido = pedido;
        this.produto = produto;
        this.quantidade = quantidade;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
