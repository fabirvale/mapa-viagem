package com.fabiana.mapa_viagem.dto;

import java.time.LocalDateTime;

public class FecharViagemDTO {
	
    private LocalDateTime dataHoraSaida;
    private LocalDateTime dataHoraAlmocoInicio;
    private LocalDateTime dataHoraAlmocoFim;
    private String localAlmoco;
    private LocalDateTime dataHoraRetorno;
     
    
	public FecharViagemDTO() {
		
	}
	
	
	public FecharViagemDTO(LocalDateTime dataHoraSaida, LocalDateTime dataHoraAlmocoInicio,	LocalDateTime dataHoraAlmocoFim,
			                String localAlmoco, LocalDateTime dataHoraRetorno) {
		this.dataHoraSaida = dataHoraSaida;
		this.dataHoraAlmocoInicio = dataHoraAlmocoInicio;
		this.dataHoraAlmocoFim = dataHoraAlmocoFim;
		this.localAlmoco = localAlmoco;
		this.dataHoraRetorno = dataHoraRetorno;
	}

	public LocalDateTime getDataHoraSaida() {
		return dataHoraSaida;
	}
	public void setDataHoraSaida(LocalDateTime dataHoraSaida) {
		this.dataHoraSaida = dataHoraSaida;
	}
	
	public LocalDateTime getDataHoraAlmocoInicio() {
		return dataHoraAlmocoInicio;
	}


	public void setDataHoraAlmocoInicio(LocalDateTime dataHoraAlmocoInicio) {
		this.dataHoraAlmocoInicio = dataHoraAlmocoInicio;
	}

	public LocalDateTime getDataHoraAlmocoFim() {
		return dataHoraAlmocoFim;
	}


	public void setDataHoraAlmocoFim(LocalDateTime dataHoraAlmocoFim) {
		this.dataHoraAlmocoFim = dataHoraAlmocoFim;
	}


	public String getLocalAlmoco() {
		return localAlmoco;
	}


	public void setLocalAlmoco(String localAlmoco) {
		this.localAlmoco = localAlmoco;
	}


	public LocalDateTime getDataHoraRetorno() {
		return dataHoraRetorno;
	}
	public void setDataHoraRetorno(LocalDateTime dataHoraRetorno) {
		this.dataHoraRetorno = dataHoraRetorno;
	}
    
    

}
