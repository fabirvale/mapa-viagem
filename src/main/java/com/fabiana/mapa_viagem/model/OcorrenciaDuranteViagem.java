package com.fabiana.mapa_viagem.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fabiana.mapa_viagem.dto.OcorrenciaDuranteViagemDTO;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

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
	
	@ManyToOne
	@JoinColumn(name = "viagem_id", insertable = false, updatable = false)
	private Viagem viagem;
	
	protected OcorrenciaDuranteViagem() {
	
	}
	
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
	
	public Viagem getViagem() {
	    return viagem;
	}

	
	public abstract BigDecimal calcularValor();
	
	 public abstract OcorrenciaDuranteViagemDTO toDTO(Long viagemId);

}
