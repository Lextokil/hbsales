package br.com.hbsis.funcionario;

import javax.persistence.*;

@Entity
@Table(name = "funcionarios")
public class Funcionario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "uuid", nullable = false)
    private String uuid;

    public Funcionario() {
    }

    public Funcionario(String nome, String email, String uuid) {
        this.nome = nome;
        this.email = email;
        this.uuid = uuid;
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

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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
