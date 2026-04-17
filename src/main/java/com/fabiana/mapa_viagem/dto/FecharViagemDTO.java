package com.fabiana.mapa_viagem.dto;

import java.time.LocalDateTime;

public class FecharViagemDTO {
	
	private Integer kmInicial;
    private Integer kmFinal;
    private LocalDateTime dataHoraSaida;
    private LocalDateTime dataHoraAlmoco;
    private LocalDateTime dataHoraRetorno;
     
    
	public FecharViagemDTO() {
		
	}
	
	
	public FecharViagemDTO(Integer kmInicial, Integer kmFinal, LocalDateTime dataHoraSaida,
			LocalDateTime dataHoraAlmoco, LocalDateTime dataHoraRetorno) {
		this.kmInicial = kmInicial;
		this.kmFinal = kmFinal;
		this.dataHoraSaida = dataHoraSaida;
		this.dataHoraAlmoco = dataHoraAlmoco;
		this.dataHoraRetorno = dataHoraRetorno;
	}


	public Integer getKmInicial() {
		return kmInicial;
	}
	public void setKmInicial(Integer kmInicial) {
		this.kmInicial = kmInicial;
	}
	public Integer getKmFinal() {
		return kmFinal;
	}
	public void setKmFinal(Integer kmFinal) {
		this.kmFinal = kmFinal;
	}
	public LocalDateTime getDataHoraSaida() {
		return dataHoraSaida;
	}
	public void setDataHoraSaida(LocalDateTime dataHoraSaida) {
		this.dataHoraSaida = dataHoraSaida;
	}
	public LocalDateTime getDataHoraAlmoco() {
		return dataHoraAlmoco;
	}
	public void setDataHoraAlmoco(LocalDateTime dataHoraAlmoco) {
		this.dataHoraAlmoco = dataHoraAlmoco;
	}
	public LocalDateTime getDataHoraRetorno() {
		return dataHoraRetorno;
	}
	public void setDataHoraRetorno(LocalDateTime dataHoraRetorno) {
		this.dataHoraRetorno = dataHoraRetorno;
	}
    
    

}
