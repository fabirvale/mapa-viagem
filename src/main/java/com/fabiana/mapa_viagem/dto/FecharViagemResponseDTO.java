package com.fabiana.mapa_viagem.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public class FecharViagemResponseDTO {
	
	private Long viagemId;
	private String motoristaNome;
	private LocalDate dataRetorno;
	private LocalTime horaChegada;
	private int kmPercorridos;
	private String tipoDiaria;
	private BigDecimal valorDiaria;
	private BigDecimal totalOcorrencias;
	
   public FecharViagemResponseDTO() {
		
	}
	
	
	public FecharViagemResponseDTO(Long viagemId, String motoristaNome, LocalDate dataRetorno, LocalTime horaChegada,
			int kmPercorridos, String tipoDiaria, BigDecimal valorDiaria, BigDecimal totalOcorrencias) {
		super();
		this.viagemId = viagemId;
		this.motoristaNome = motoristaNome;
		this.dataRetorno = dataRetorno;
		this.horaChegada = horaChegada;
		this.kmPercorridos = kmPercorridos;
		this.tipoDiaria = tipoDiaria;
		this.valorDiaria = valorDiaria;
		this.totalOcorrencias = totalOcorrencias;
	}


	public Long getViagemId() {
		return viagemId;
	}


	public void setViagemId(Long viagemId) {
		this.viagemId = viagemId;
	}


	public String getMotoristaNome() {
		return motoristaNome;
	}


	public void setMotoristaNome(String motoristaNome) {
		this.motoristaNome = motoristaNome;
	}


	public LocalDate getDataRetorno() {
		return dataRetorno;
	}


	public void setDataRetorno(LocalDate dataRetorno) {
		this.dataRetorno = dataRetorno;
	}


	public LocalTime getHoraChegada() {
		return horaChegada;
	}


	public void setHoraChegada(LocalTime horaChegada) {
		this.horaChegada = horaChegada;
	}


	public int getKmPercorridos() {
		return kmPercorridos;
	}


	public void setKmPercorridos(int kmPercorridos) {
		this.kmPercorridos = kmPercorridos;
	}


	public String getTipoDiaria() {
		return tipoDiaria;
	}


	public void setTipoDiaria(String tipoDiaria) {
		this.tipoDiaria = tipoDiaria;
	}


	public BigDecimal getValorDiaria() {
		return valorDiaria;
	}


	public void setValorDiaria(BigDecimal valorDiaria) {
		this.valorDiaria = valorDiaria;
	}


	public BigDecimal getTotalOcorrencias() {
		return totalOcorrencias;
	}


	public void setTotalOcorrencias(BigDecimal totalOcorrencias) {
		this.totalOcorrencias = totalOcorrencias;
	}
	
	

	
	
	

}
