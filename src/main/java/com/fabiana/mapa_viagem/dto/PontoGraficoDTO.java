package com.fabiana.mapa_viagem.dto;

import java.math.BigDecimal;

public class PontoGraficoDTO {
	private String rotulo;
    private BigDecimal valor;
    
    public PontoGraficoDTO() {
    	
	}
    
    
	public PontoGraficoDTO(String rotulo, BigDecimal valor) {
		this.rotulo = rotulo;
		this.valor = valor;
	}
	
	public PontoGraficoDTO(Enum<?> rotulo, Long quantidade) {
	    this.rotulo = rotulo.name();
	    this.valor = BigDecimal.valueOf(quantidade);
	}
	
	public PontoGraficoDTO(String rotulo, Long quantidade) {
	    this.rotulo = rotulo;
	    this.valor = BigDecimal.valueOf(quantidade);
	}


	public String getRotulo() {
		return rotulo;
	}


	public BigDecimal getValor() {
		return valor;
	}

	
    
}
