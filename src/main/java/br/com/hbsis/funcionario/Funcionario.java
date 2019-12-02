package br.com.hbsis.funcionario;

import javax.persistence.*;

@Entity
public class Funcionario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "email", nullable = false)
    private String email;

    public Funcionario() {
    }

    public Funcionario(String nome, String email) {
        this.nome = nome;
        this.email = email;
    }

    public Funcionario(Long id, String nome, String email) {
        this.id = id;
        this.nome = nome;
        this.email = email;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    @Override
    public String toString() {
        return "Funcionario{" +
                "id=" + id +
                ", Nome ='" + getNome() + '\'' +
                ", E-mail ='" + getEmail() + '\'' +
                '}';
    }
}
