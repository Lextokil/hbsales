package br.com.hbsis.categoriaprodutos;

public class CategoriaProdutoDTO {

    private Long id;
    private String codCategoria;
    private String nome;
    private Long fornecedor;

    public CategoriaProdutoDTO() {
    }


    public CategoriaProdutoDTO(Long id, String codCategoria, String nome, Long fornecedor) {
        this.id = id;
        this.codCategoria = codCategoria;
        this.nome = nome;
        this.fornecedor = fornecedor;
    }

    public static CategoriaProdutoDTO of(CategoriaProduto categoriaProduto) {
        return new CategoriaProdutoDTO(categoriaProduto.getId(),
                categoriaProduto.getCodCategoria(),
                categoriaProduto.getNome(),
                categoriaProduto.getFornecedor().getId());

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

    public String getCodCategoria() {

        return codCategoria;
    }

    public void setCodCategoria(String codCategoria) {
        this.codCategoria = codCategoria;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Long getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(Long fornecedor) {
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
