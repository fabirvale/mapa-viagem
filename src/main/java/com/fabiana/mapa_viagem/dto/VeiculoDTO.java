package com.fabiana.mapa_viagem.dto;

import com.fabiana.mapa_viagem.model.Veiculo;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class VeiculoDTO {
	private Long  id;
	
	@NotBlank
	private String placa;
	
	@NotBlank
	private String modelo;
	
	@Min(1)
	private Integer capacidadePassageiros;
	
	public VeiculoDTO() {
	
	}

	public VeiculoDTO(Veiculo veiculo) {
		this.id = veiculo.getId();
		this.placa = veiculo.getPlaca();
		this.modelo = veiculo.getModelo();
		this.capacidadePassageiros = veiculo.getCapacidadePassageiros();
	}

	public Long getId() {
		return id;
	}

	public String getPlaca() {
		return placa;
	}
	
	public void setPlaca(String placa) {
		this.placa = placa;
	}
	
	
	public String getModelo() {
		return modelo;
	}

	public void setModelo(String modelo) {
		this.modelo = modelo;
	}
	
	public Integer getCapacidadePassageiros() {
		return capacidadePassageiros;
	}

	public void setCapacidadePassageiros(Integer capacidadePassageiros) {
		this.capacidadePassageiros = capacidadePassageiros;
	}


}
