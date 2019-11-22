package br.com.hbsis.fornecedor;

import org.springframework.data.annotation.Id;

import javax.persistence.*;

@Entity
@Table(name = "fornecedores")
public class Fornecedor {

    public Fornecedor() {
    }

    public Fornecedor(String razaoSocial, String cnpj, String nome, String endereco, String telefone, String email) {
        this.razaoSocial = razaoSocial;
        this.cnpj = cnpj;
        this.nome = nome;
        this.endereco = endereco;
        this.telefone = telefone;
        this.email = email;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "razao_social")
    private String razaoSocial;

    @Column(name = "cnpj", unique = true, nullable = false, length = 50)
    private String cnpj;

    @Column(name = "nome", length = 100, nullable = false)
    private String nome;

    @Column(name = "endereco", nullable = false)
    private String endereco;

    @Column(name = "telefone", length = 20, nullable = false)
    private String telefone;

    @Column(name = "email", nullable = false)
    private String email;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Fornecedor{" +
                "id=" + id +
                ", Razao Social ='" + razaoSocial + '\'' +
                ", CNPJ ='" + cnpj + '\'' +
                ", Nome ='" + nome + '\'' +
                ", Endereço ='" + endereco + '\'' +
                ", Telefone ='" + telefone + '\'' +
                ", E-mail ='" + email + '\'' +
                '}';
    }
}
