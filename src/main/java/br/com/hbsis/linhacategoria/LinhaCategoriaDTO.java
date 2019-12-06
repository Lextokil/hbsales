package br.com.hbsis.linhacategoria;

public class LinhaCategoriaDTO {

    private Long idLinhaCategoria;
    private String codLinha;
    private Long categoriaProduto;
    private String nomeLinha;


    public LinhaCategoriaDTO() {
    }


    public LinhaCategoriaDTO(Long idLinhaCategoria, String codLinha, String nomeLinha, Long categoriaProduto) {
        this.idLinhaCategoria = idLinhaCategoria;
        this.codLinha = codLinha;
        this.categoriaProduto = categoriaProduto;
        this.nomeLinha = nomeLinha;
    }

    public Long getIdLinhaCategoria() {
        return idLinhaCategoria;
    }

    public void setIdLinhaCategoria(Long idLinhaCategoria) {
        this.idLinhaCategoria = idLinhaCategoria;
    }

    public String getCodLinha() {
        return codLinha;
    }

    public void setCodLinha(String codLinha) {
        this.codLinha = codLinha;
    }

    public Long getCategoriaProduto() {
        return categoriaProduto;
    }

    public void setCategoriaProduto(Long categoriaProduto) {
        this.categoriaProduto = categoriaProduto;
    }

    public String getNomeLinha() {
        return nomeLinha;
    }

    public void setNomeLinha(String nomeLinha) {
        this.nomeLinha = nomeLinha;
    }

    public static LinhaCategoriaDTO of(LinhaCategoria linhaCategoria) {
        return new LinhaCategoriaDTO(
                        linhaCategoria.getIdLinhaCategoria(),
                        linhaCategoria.getCodLinha(),
                        linhaCategoria.getNomeLinha(),
                        linhaCategoria.getCategoriaProduto().getId());

    }
}
