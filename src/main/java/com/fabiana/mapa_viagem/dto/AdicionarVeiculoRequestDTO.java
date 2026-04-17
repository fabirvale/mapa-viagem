package com.fabiana.mapa_viagem.dto;

public class AdicionarVeiculoRequestDTO {
	
	private Long veiculoId;

	public AdicionarVeiculoRequestDTO() {
	
	}

	public AdicionarVeiculoRequestDTO(Long veiculoId) {
		this.veiculoId = veiculoId;
	}

	public Long getVeiculoId() {
		return veiculoId;
	}

	public void setVeiculoId(Long veiculoId) {
		this.veiculoId = veiculoId;
	}
	
	

}
