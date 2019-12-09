package br.com.hbsis.produtos;

import br.com.hbsis.linhacategoria.LinhaCategoria;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "produtos")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "cod_produto", nullable = false, length = 10, unique = true)
    private String codProduto;

    @Column(name = "nome_produto", nullable = false, length = 200)
    private String nomeProduto;

    @Column(name = "preco_produto", nullable = false)
    private Double precoProduto;

    @Column(name = "unidade_caixa", nullable = false)
    private int unidadeProduto;

    @Column(name = "peso_unidade", nullable = false)
    private  Double pesoUnidade;

    @Column(name = "unidade_medida", nullable = false)
    private String unidadeMedida;

    @JsonFormat(pattern="dd-MM-yyyy HH:mm:ss")
    @Column(name = "validade_produto")
    private LocalDateTime validadeProduto;

    @ManyToOne
    @JoinColumn(name = "id_linha", referencedColumnName = "id")
    private LinhaCategoria linhaCategoria;

    public Produto() {
    }

    public Produto(String codProduto, String nomeProduto, Double precoProduto, int unidadeProduto, Double pesoUnidade,
                   String unidadeMedida, LocalDateTime validadeProduto, LinhaCategoria linhaCategoria) {
        this.codProduto = codProduto;
        this.nomeProduto = nomeProduto;
        this.precoProduto = precoProduto;
        this.unidadeProduto = unidadeProduto;
        this.pesoUnidade = pesoUnidade;
        this.unidadeMedida = unidadeMedida;
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

    public void setPrecoProduto(Double precoProduto) {
        this.precoProduto = precoProduto;
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

    public String getUnidadeMedida() {
        return unidadeMedida;
    }

    public void setUnidadeMedida(String unidadeMedida) {
        this.unidadeMedida = unidadeMedida;
    }

    public LocalDateTime getValidadeProduto() {
        return validadeProduto;
    }

    public void setValidadeProduto(LocalDateTime validadeProduto) {
        this.validadeProduto = validadeProduto;
    }

    public LinhaCategoria getLinhaCategoria() {
        return linhaCategoria;
    }

    public void setLinhaCategoria(LinhaCategoria linhaCategoria) {
        this.linhaCategoria = linhaCategoria;
    }
}
