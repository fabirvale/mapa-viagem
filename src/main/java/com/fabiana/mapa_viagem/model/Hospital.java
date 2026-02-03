package com.fabiana.mapa_viagem.model;

import com.fabiana.mapa_viagem.enums.TipoEstabelecimento;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Hospital {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String nome;
	private String cidade;
	
	@Enumerated(EnumType.STRING)
	private TipoEstabelecimento tipo;
	
	public Hospital(Long id, String nome, String cidade, TipoEstabelecimento tipo) {
		this.id = id;
		this.nome = nome;
		this.cidade = cidade;
		this.tipo = tipo;
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
