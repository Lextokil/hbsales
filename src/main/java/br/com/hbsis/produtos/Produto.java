package br.com.hbsis.produtos;

import br.com.hbsis.linhacategoria.LinhaCategoria;

import javax.persistence.*;

@Entity
@Table(name = "produtos")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "cod_produto")
    private String codProduto;

    @Column(name = "nome_produto")
    private String nomeProduto;

    @Column(name = "preco_produto")
    private Double precoProduto;

    @Column(name = "unidade_caixa")
    private int unidadeProduto;

    @Column(name = "peso_unidade")
    private  Double pesoUnidade;

    @Column(name = "validade_produto")
    private String validadeProduto;

    @ManyToOne
    @JoinColumn(name = "id_linha", referencedColumnName = "id")
    private LinhaCategoria linhaCategoria;

    public Produto() {
    }

    public Produto(String codProduto, String nomeProduto, Double precoProduto,
                   int unidadeProduto, Double pesoUnidade, String validadeProduto,
                   LinhaCategoria linhaCategoria) {
        this.codProduto = codProduto;
        this.nomeProduto = nomeProduto;
        this.precoProduto = precoProduto;
        this.unidadeProduto = unidadeProduto;
        this.pesoUnidade = pesoUnidade;
        this.validadeProduto = validadeProduto;
        this.linhaCategoria = linhaCategoria;
    }




    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodProduto() {
        return codProduto;
    }

    public void setCodProduto(String codProduto) {
        this.codProduto = codProduto;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public Double getPrecoProduto() {
        return precoProduto;
    }

    public void setPrecoProduto(Double preco_produto) {
        this.precoProduto = preco_produto;
    }

    public int getUnidadeProduto() {
        return unidadeProduto;
    }

    public void setUnidadeProduto(int unidadeProduto) {
        this.unidadeProduto = unidadeProduto;
    }

    public Double getPesoUnidade() {
        return pesoUnidade;
    }

    public void setPesoUnidade(Double pesoUnidade) {
        this.pesoUnidade = pesoUnidade;
    }

    public String getValidadeProduto() {
        return validadeProduto;
    }

    public void setValidadeProduto(String validadeProduto) {
        this.validadeProduto = validadeProduto;
    }

    public LinhaCategoria getLinhaCategoria() {
        return linhaCategoria;
    }

    public void setLinhaCategoria(LinhaCategoria linhaCategoria) {
        this.linhaCategoria = linhaCategoria;
    }
}
