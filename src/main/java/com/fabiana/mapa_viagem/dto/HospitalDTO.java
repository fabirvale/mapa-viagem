package com.fabiana.mapa_viagem.dto;

import com.fabiana.mapa_viagem.enums.TipoEstabelecimento;
import com.fabiana.mapa_viagem.model.Hospital;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class HospitalDTO {
	private Long id;
	
	@NotBlank(message = "Nome é obrigatório")
	private String nome;

	@NotBlank(message = "Endereço é obrigatório")
	private String endereco;

	@NotBlank(message = "Número é obrigatório")
	private String numero;
	
	private String complemento;

	@NotBlank(message = "Bairro é obrigatório")
	private String bairro;

	@NotBlank(message = "CEP é obrigatório")
	@Size(min = 8, max = 9, message = "CEP deve ter 8 ou 9 caracteres")
	private String cep;

	@NotBlank(message = "Cidade é obrigatória")
	private String cidade;

	@NotBlank(message = "Telefone é obrigatório")
	private String telefone;

	@NotNull(message = "Tipo do estabelecimento é obrigatório")
	private TipoEstabelecimento tipo;
	

	public HospitalDTO() {
	
	}

	public HospitalDTO(Hospital hospital) {
		this.id   = hospital.getId();
		this.nome = hospital.getNome();
		this.endereco = hospital.getEndereco();
		this.numero = hospital.getNumero();
		this.complemento = hospital.getComplemento();
		this.bairro = hospital.getBairro();
		this.cep = hospital.getCep();
		this.cidade = hospital.getCidade();
		this.telefone = hospital.getTelefone();
		this.tipo = hospital.getTipo();
	}

	
	public Long getId() {
		return id;
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

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}
	
	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}


	public TipoEstabelecimento getTipo() {
		return tipo;
	}

	public void setTipo(TipoEstabelecimento tipo) {
		this.tipo = tipo;
	}
	
	

}
