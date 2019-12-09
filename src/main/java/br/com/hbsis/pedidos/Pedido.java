package br.com.hbsis.pedidos;

import br.com.hbsis.itempedido.ItemPedido;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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

    @OneToMany(cascade = CascadeType.ALL,orphanRemoval = true)
    @JoinColumn(name = "id_pedido", referencedColumnName = "id")
    private Set<ItemPedido> itensPedido = new HashSet<>();

    @Column(name = "valor_total")
    private double valorTotal;

    public Pedido() {
    }

    public Pedido(Set<ItemPedido> itemPedidoSet, double valorTotal) {
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

    public Set<ItemPedido> getItensPedido() {
        return itensPedido;
    }

    public void setItensPedido(Set<ItemPedido> itensPedido) {
        this.itensPedido = itensPedido;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }
}
