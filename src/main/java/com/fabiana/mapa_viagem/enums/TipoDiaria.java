package com.fabiana.mapa_viagem.enums;

import java.math.BigDecimal;

public enum TipoDiaria {

	    PARCIAL_SEM_PERNOITE(5, 8, new BigDecimal("56.00"), "01 refeição, 01 lanche e locomoção urbana"),

	    COMPLETA_SEM_PERNOITE(8, null, new BigDecimal("205.00"), "02 refeições, 01 lanche e locomoção urbana"),

	    COMPLETA_COM_PERNOITE(null, null, new BigDecimal("420.00"), "02 refeições, 01 lanche, 01 pernoite e locomoção urbana");

	    private final Integer horasMin;
	    private final Integer horasMax;
	    private final BigDecimal valor;
	    private final String descricao;

	    private TipoDiaria(Integer horasMin, Integer horasMax, BigDecimal valor, String descricao) {
			this.horasMin = horasMin;
			this.horasMax = horasMax;
			this.valor = valor;
			this.descricao = descricao;
		}

		public BigDecimal getValor() {
	        return valor;
	    }

	    public String getDescricao() {
	        return descricao;
	    }

	    public boolean aplicaPara(int horas) {
	        if (horasMin != null && horas < horasMin) return false;
	        if (horasMax != null && horas > horasMax) return false;
	        return true;
	    }
	}

