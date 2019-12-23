package br.com.hbsis.pedidos;

import br.com.hbsis.itempedido.ItemPedido;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "pedidos")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "id_pedido", referencedColumnName = "id")
    private Set<ItemPedido> itensPedido = new HashSet<>();

    @Column(name = "status")
    private String status;

    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao;

    @Column(name = "codigo")
    private String codigo;

    @Column(name = "valor_total")
    private double valorTotal;

    public Pedido() {
    }

    public Pedido(Set<ItemPedido> itensPedido, String status, LocalDateTime dataCriacao, String codigo, double valorTotal) {
        this.itensPedido = itensPedido;
        this.status = status;
        this.dataCriacao = dataCriacao;
        this.codigo = codigo;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }
}
