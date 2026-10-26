package com.fabiana.mapa_viagem.enums;

import java.math.BigDecimal;

public enum TipoDiaria {
	    
	    SEM_DIARIA(null, null, new BigDecimal("0.00"), "Sem diária"),
     
	    PARCIAL_SEM_PERNOITE(300, 480, new BigDecimal("56.00"), "01 refeição, 01 lanche e locomoção urbana"),

	    COMPLETA_SEM_PERNOITE(480, null, new BigDecimal("105.00"), "02 refeições, 01 lanche e locomoção urbana"),

	    COMPLETA_COM_PERNOITE(null, null, new BigDecimal("420.00"), "02 refeições, 01 lanche, 01 pernoite e locomoção urbana");

	    private final Integer minutosMin;
	    private final Integer minutosMax;
	    private final BigDecimal valor;
	    private final String descricao;

	    private TipoDiaria(Integer minutosMin, Integer minutosMax,
	            BigDecimal valor, String descricao) {
	        this.minutosMin = minutosMin;
	        this.minutosMax = minutosMax;
	        this.valor = valor;
	        this.descricao = descricao;
	    }

	    public BigDecimal getValor() {
	        return valor;
	    }

	    public String getDescricao() {
	        return descricao;
	    }

	    public boolean aplicaPara(int minutos) {
	    	
	        if (minutosMin != null && minutos < minutosMin) return false;
	        if (minutosMax != null && minutos >= minutosMax) return false;   
	        return true;
	    }
	
}

