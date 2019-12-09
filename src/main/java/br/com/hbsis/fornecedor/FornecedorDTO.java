package br.com.hbsis.fornecedor;

import br.com.hbsis.validation.CnpjValidation;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class FornecedorDTO {

	private Long id;
	@Size(max = 100)
	@NotBlank(message = "razaoSocial é obrigatória")
	private String razaoSocial;
	@CnpjValidation(length = 14, message = "Deve conter somente números e exatamente 14 caracteres")
	private String cnpj;
	@Size(max = 100)
	@NotBlank(message = "nomeFantasia é obrigatório")
	private String nomeFantasia;
	@NotBlank(message = "endereco é obrigatório")
	private String endereco;
	@NotBlank
	@Pattern(regexp = "\\d{11}", message = "Telefone de contato deve ter 11 caracteres numéricos")
	private String telefoneContato;
	@NotBlank(message = "e-mail de contato é obrigatório")
	private String emailContato;

	private FornecedorDTO() {
	}

	public FornecedorDTO(Long id, String razaoSocial, String cnpj, String nomeFantasia, String endereco, String telefoneContato, String emailContato) {
		this.id = id;
		this.razaoSocial = razaoSocial;
		this.cnpj = cnpj;
		this.nomeFantasia = nomeFantasia;
		this.endereco = endereco;
		this.telefoneContato = telefoneContato;
		this.emailContato = emailContato;
	}

	public FornecedorDTO(String razaoSocial, String cnpj,String nomeFantasia,String endereco, String telefoneContato, String emailContato) {
		this.razaoSocial = razaoSocial;
		this.cnpj = cnpj;
		this.nomeFantasia = nomeFantasia;
		this.endereco = endereco;
		this.telefoneContato = telefoneContato;
		this.emailContato = emailContato;
	}

	public static FornecedorDTO of(Fornecedor fornecedor) {
		return new FornecedorDTO(
				fornecedor.getId(),
				fornecedor.getRazaoSocial(),
				fornecedor.getCnpj(),
				fornecedor.getNomeFantasia(),
				fornecedor.getEndereco(),
				fornecedor.getTelefoneContato(),
				fornecedor.getEmailContato()
		);
	}

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

	public String getTelefoneContato() {
		return telefoneContato;
	}

	public void setTelefoneContato(String telefoneContato) {
		this.telefoneContato = telefoneContato;
	}

	public String getEmailContato() {
		return emailContato;
	}

	public void setEmailContato(String emailContato) {
		this.emailContato = emailContato;
	}

	@Override
	public String toString() {
		return "FornecedorDTO{" +
				"id=" + id +
				", razaoSocial='" + razaoSocial + '\'' +
				", cnpj='" + cnpj + '\'' +
				", nomeFantasia='" + nomeFantasia + '\'' +
				", endereco='" + endereco + '\'' +
				", contato='" + telefoneContato + '\'' +
				'}';
	}
}
