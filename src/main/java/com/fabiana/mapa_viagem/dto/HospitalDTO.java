package com.fabiana.mapa_viagem.dto;

import com.fabiana.mapa_viagem.enums.TipoEstabelecimento;
import com.fabiana.mapa_viagem.model.Hospital;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public class HospitalDTO {
	private Long id;
	private String nome;
	private String endereco;
	private String numero;
	private String complemento;
	private String bairro;
	private String cep;
	private String cidade;
	
	//HOSPITAL, CLINICA, UPA, UBS, 	LABORATORIO
	@Enumerated(EnumType.STRING)
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

	public TipoEstabelecimento getTipo() {
		return tipo;
	}

	public void setTipo(TipoEstabelecimento tipo) {
		this.tipo = tipo;
	}
	
	

}
