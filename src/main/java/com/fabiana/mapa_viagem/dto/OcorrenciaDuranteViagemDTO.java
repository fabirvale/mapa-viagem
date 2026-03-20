package com.fabiana.mapa_viagem.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class OcorrenciaDuranteViagemDTO {
	
	private Long id;
	
	@NotBlank(message = "Descrição é obrigatória")
	@Size(max = 255, message = "Descrição deve ter no máximo 255 caracteres")
	private String descricao;
	
	@NotNull(message = "Data da viagem é obrigatória")
	private LocalDate data;
	
	@NotNull(message = "Valor é obrigatório")
	@DecimalMin(value = "0.01", message = "Valor deve ser maior que zero")
	private BigDecimal valor;
	
	@NotNull(message = "Id da viagem é obrigatório")
	private Long viagemId;
	
	protected OcorrenciaDuranteViagemDTO() {
	
	}
	
	public OcorrenciaDuranteViagemDTO(Long id, String descricao, LocalDate data, BigDecimal valor, Long viagemId) {
		this.id = id;
		this.descricao = descricao;
		this.data = data;
		this.valor = valor;
		this.viagemId = viagemId;
	}

	public Long getId() {
		return id;
	}
			
	public Long getViagemId() {
		return viagemId;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public LocalDate getData() {
		return data;
	}

	public void setData(LocalDate data) {
		this.data = data;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
	
	

}
