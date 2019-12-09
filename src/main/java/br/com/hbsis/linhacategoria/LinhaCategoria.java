package br.com.hbsis.linhacategoria;

import br.com.hbsis.categoriaprodutos.CategoriaProduto;

import javax.persistence.*;

@Entity
@Table(name = "linha_categoria")
public class LinhaCategoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long idLinhaCategoria;

    @Column(name = "cod_linha", nullable = false, length = 10)
    private String codLinha;

    @ManyToOne
    @JoinColumn(name = "id_categoria", referencedColumnName = "id", nullable = false)
    private CategoriaProduto categoriaProduto;

    @Column(name = "nome_linha", nullable = false, length = 50)
    private String nomeLinha;

    public LinhaCategoria() {
    }

    public LinhaCategoria(String codLinha,  String nomeLinha, CategoriaProduto categoriaProduto) {
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

    public CategoriaProduto getCategoriaProduto() {
        return categoriaProduto;
    }

    public void setCategoriaProduto(CategoriaProduto categoriaProduto) {
        this.categoriaProduto = categoriaProduto;
    }

    public String getNomeLinha() {
        return nomeLinha;
    }

    public void setNomeLinha(String nomeLinha) {
        this.nomeLinha = nomeLinha;
    }


}
