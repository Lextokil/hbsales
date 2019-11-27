package br.com.hbsis.produtos;

import br.com.hbsis.fornecedor.Fornecedor;
import com.opencsv.bean.CsvBindByPosition;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;

@Entity
@Table(name = "produtos")
public class CategoriaProduto {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "cod_categoria", unique = true, nullable = false)
    private String codCategoria;

    @Column(name = "nome", unique = true, nullable = false, length = 100)
    private String nome;

    @Column(name= "id_fornecedor" , nullable = false, insertable = false, updatable = false)
    private  Long id_fornecedor;

    @ManyToOne
    @JoinColumn(name = "id_fornecedor", referencedColumnName = "id")
    private Fornecedor fornecedor;

    @Transient
    private MultipartFile file;

    public CategoriaProduto() {
    }

    public CategoriaProduto(String codCategoria, String nome, Fornecedor fornecedor) {
        this.codCategoria = codCategoria;
        this.nome = nome;
        this.fornecedor= fornecedor;
    }


    public CategoriaProduto(Long id, String codCategoria, String nome, Long id_fornecedor, Fornecedor fornecedor) {
        this.id = id;
        this.codCategoria = codCategoria;
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

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }


    @Override
    public String toString() {
        return this.getId() +";" +this.getNome() + ";" +this.getCodCategoria() + ";" +this.getId_fornecedor()+";" +
                this.getFornecedor().getNome();
    }
}
