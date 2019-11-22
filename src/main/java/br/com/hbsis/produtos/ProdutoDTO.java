package br.com.hbsis.produtos;

import br.com.hbsis.fornecedor.Fornecedor;

public class ProdutoDTO {

    private Long id;
    private String nome;
    private Fornecedor fornecedor;

    public ProdutoDTO() {
    }

    public ProdutoDTO(String nome, Fornecedor fornecedor) {
        this.nome = nome;
        this.fornecedor = fornecedor;
    }

    public static ProdutoDTO of(Produto produto) {
        return new ProdutoDTO(produto.getNome(),
                produto.getFornecedor());

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
}
