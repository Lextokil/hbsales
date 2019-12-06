package br.com.hbsis.fornecedor;

import javax.persistence.*;

@Entity
@Table(name = "fornecedores")
public class Fornecedor {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", updatable = false, insertable = false)
	private Long id;

	@Column(name = "razao_social", nullable = false, length = 100)
	private String razaoSocial;

	@Column(name = "cnpj", nullable = false, unique = true, length = 14)
	private String cnpj;

	@Column(name = "nome", nullable = false, length = 100)
	private String nomeFantasia;

	@Column(name = "endereco", length = 100)
	private String endereco;

	@Column(name = "email", length = 11)
	private String emailContato;

	@Column(name = "telefone", length = 50)
	private String telefoneContato;

	public Fornecedor() {
	}

	public Long getId() {
		return id;
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

	public String getNomeFantasia() {
		return nomeFantasia;
	}

	public void setNomeFantasia(String nomeFantasia) {
		this.nomeFantasia = nomeFantasia;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public String getEmailContato() {
		return emailContato;
	}

	public void setEmailContato(String contato) {
		this.emailContato = contato;
	}

	public String getTelefoneContato() {
		return telefoneContato;
	}

	public void setTelefoneContato(String telefoneContato) {
		this.telefoneContato = telefoneContato;
	}

	@Override
	public String toString() {
		return "Fornecedor{" +
				"id=" + id +
				", razaoSocial='" + razaoSocial + '\'' +
				", cnpj='" + cnpj + '\'' +
				", nomeFantasia='" + nomeFantasia + '\'' +
				", endereco='" + endereco + '\'' +
				", emailContato='" + emailContato + '\'' +
				", telefoneContato='" + telefoneContato + '\'' +
				'}';
	}
}
