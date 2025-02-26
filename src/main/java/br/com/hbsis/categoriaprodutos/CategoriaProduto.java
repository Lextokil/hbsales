package br.com.hbsis.categoriaprodutos;

import br.com.hbsis.fornecedor.Fornecedor;

import javax.persistence.*;

@Entity
@Table(name = "categorias")
public class CategoriaProduto {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "cod_categoria", unique = true, nullable = false, length = 10)
    private String codCategoria;

    @Column(name = "nome", unique = true, nullable = false, length = 50)
    private String nome;

    @ManyToOne
    @JoinColumn(name = "id_fornecedor", referencedColumnName = "id")
    private Fornecedor fornecedor;



    public CategoriaProduto() {
    }

    public CategoriaProduto(String codCategoria, String nome, Fornecedor fornecedor) {
        this.codCategoria = codCategoria;
        this.nome = nome;
        this.fornecedor= fornecedor;
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

    public String getCodCategoria() {
        return codCategoria;
    }

    public void setCodCategoria(String codCategoria) {
        this.codCategoria = codCategoria;
    }

    public Fornecedor getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }



    @Override
    public String toString() {
        return "Categoria:"+'\''+
                "ID: "+this.getId()+'\''+
                "Nome: "+this.getNome()+'\''+
                "Codigo: "+this.codCategoria+'\''+
                "ID fornecedor: "+this.getFornecedor().getId();
    }
}
