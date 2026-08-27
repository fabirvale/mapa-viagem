package com.fabiana.mapa_viagem.dto;

import com.fabiana.mapa_viagem.model.MotivoCancelamento;

public class MotivoCancelamentoDTO {
	
	private Long id;
	private String descricao;
	
	public MotivoCancelamentoDTO() {
	
	}
	
	public MotivoCancelamentoDTO (MotivoCancelamento motivoCancelamento) {
		this.id = motivoCancelamento.getId();
		this.descricao = motivoCancelamento.getDescricao();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	
	
	

}
