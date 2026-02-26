package com.fabiana.mapa_viagem.dto;

import com.fabiana.mapa_viagem.model.Veiculo;

public class VeiculoDTO {
	private Long  id;
	private String placa;
	private String modelo;
	
	public VeiculoDTO() {
	
	}

	public VeiculoDTO(Veiculo veiculo) {
		this.id = veiculo.getId();
		this.placa = veiculo.getPlaca();
		this.modelo = veiculo.getModelo();
	}

	public Long getId() {
		return id;
	}

	public String getPlaca() {
		return placa;
	}

	
	public String getModelo() {
		return modelo;
	}

	public void setModelo(String modelo) {
		this.modelo = modelo;
	}
	
	
	

}
