package com.fabiana.mapa_viagem.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;

@Entity
//herança entre entidades no JPA
@Inheritance(strategy = InheritanceType.JOINED)
public abstract  class OcorrenciaDuranteViagem {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
   	private Long id;
	
	private String descricao;
	private LocalDate data;
	private BigDecimal valor;
	
	public OcorrenciaDuranteViagem(Long id, String descricao, LocalDate data, BigDecimal valor) {
		this.id = id;
		this.descricao = descricao;
		this.data = data;
		this.valor = valor;
	}

	public Long getId() {
		return id;
	}

	
	public String getDescricao() {
		return descricao;
	}

	
	public LocalDate getData() {
		return data;
	}

	
	public BigDecimal getValor() {
		return valor;
	}

	
	public abstract BigDecimal calcularValor();

}
