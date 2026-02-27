package com.fabiana.mapa_viagem.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Veiculo {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long  id;
	private String placa;
	private String modelo;
	private Integer capacidadePassageiros;
	
	public Veiculo() {
	
	}

	public Veiculo(String placa, String modelo, Integer capacidadePassageiros) {
		this.placa = placa;
		this.modelo = modelo;
		this.capacidadePassageiros = capacidadePassageiros;
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

	public int getCapacidadePassageiros() {
		return capacidadePassageiros;
	}
	
	public void setCapacidadePassageiros(Integer capacidadePassageiros) {
		this.capacidadePassageiros = capacidadePassageiros;
	}

	
}
