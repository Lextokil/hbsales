package br.com.hbsis.produtos;

import br.com.hbsis.util.DateValidator;


public class ProdutoDTO {
    private  Long idProduto;
    private String codProduto;
    private String nomeProduto;
    private Double precoProduto;
    private int unidadeProduto;
    private Double pesoUnidade;
    private String unidadeMedida;
    private String validadeProduto;
    private Long linhaCategoria;

    public ProdutoDTO() {
    }

    public ProdutoDTO(Long idProduto, String codProduto, String nomeProduto, Double precoProduto, int unidadeProduto,
                      Double pesoUnidade, String unidadeMedida, String validadeProduto, Long linhaCategoria) {
        this.idProduto = idProduto;
        this.codProduto = codProduto;
        this.nomeProduto = nomeProduto;
        this.precoProduto = precoProduto;
        this.unidadeProduto = unidadeProduto;
        this.pesoUnidade = pesoUnidade;
        this.unidadeMedida = unidadeMedida;
        this.validadeProduto = validadeProduto;
        this.linhaCategoria = linhaCategoria;
    }

    public static ProdutoDTO of(Produto produto) {
        return new ProdutoDTO(
                produto.getId(),
                produto.getCodProduto(),
                produto.getNomeProduto(),
                produto.getPrecoProduto(),
                produto.getUnidadeProduto(),
                produto.getPesoUnidade(),
                produto.getUnidadeMedida(),
                DateValidator.convertDateToString(produto.getValidadeProduto()),
                produto.getLinhaCategoria().getIdLinhaCategoria());
    }

    public Long getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(Long idProduto) {
        this.idProduto = idProduto;
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

    public String getValidadeProduto() {
        return validadeProduto;
    }

    public void setValidadeProduto(String validadeProduto) {
        this.validadeProduto = validadeProduto;
    }

    public Long getLinhaCategoria() {
        return linhaCategoria;
    }

    public void setLinhaCategoria(Long linhaCategoria) {
        this.linhaCategoria = linhaCategoria;
    }
}
