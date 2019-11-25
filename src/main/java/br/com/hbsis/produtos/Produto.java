package br.com.hbsis.produtos;

import br.com.hbsis.fornecedor.Fornecedor;

import javax.persistence.*;

@Entity
@Table(name = "produtos")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", unique = true, nullable = false, length = 100)
    private String nome;

    @Column(name= "id_fornecedor" , nullable = false, insertable = false, updatable = false)
    private  Long id_fornecedor;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_fornecedor", referencedColumnName = "id")
    private Fornecedor fornecedor;

    public Produto() {
    }

    public Produto(String nome, Fornecedor fornecedor) {
        this.nome = nome;
        this.fornecedor= fornecedor;
    }

    public Produto(Long id, String nome, Long id_fornecedor, Fornecedor fornecedor) {
        this.id = id;
        this.nome = nome;
        this.id_fornecedor = id_fornecedor;
        this.fornecedor = fornecedor;
    }

    public Long getId_fornecedor() {
        return id_fornecedor;
    }

    public void setId_fornecedor(Long id_fornecedor) {
        this.id_fornecedor = id_fornecedor;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Fornecedor getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }

    @Override
    public String toString() {
        return "Produto{" +
                "id=" + id +
                ", Nome ='" + nome + '\'' +
                ", Fornecedor: ='" + fornecedor + '\'' +
                '}';
    }
}
