package br.com.hbsis.pedidos;

import br.com.hbsis.fornecedor.Fornecedor;
import br.com.hbsis.itempedido.ItemPedido;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "pedidos")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;


    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name="item_pedido",
            joinColumns = @JoinColumn( name="id"),
            inverseJoinColumns = @JoinColumn( name="id_pedido")
    )
    private Set<ItemPedido> itensPedido;

    @Column(name = "valor_total", nullable = false)
    private double valorTotal;

    public Pedido() {
    }

    public Pedido( Set<ItemPedido> itemPedidoSet, double valorTotal) {
        this.itensPedido = itemPedidoSet;
        this.valorTotal = valorTotal;
    }

    public Pedido(double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<ItemPedido> getItemPedidoSet() {
        return itensPedido;
    }

    public void setItemPedidoSet(Set<ItemPedido> itemPedidoSet) {
        this.itensPedido = itemPedidoSet;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }
}
