package com.fabiana.mapa_viagem.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class LinhaResumoDTO {
	
	private Long viagemId;
    private LocalDate data;
    private String rota;
    LocalDateTime saida;
    LocalDateTime retorno;
    Integer kmPercorridos;
    private String tipoDiaria;
    private BigDecimal valorDiaria;
    
    public LinhaResumoDTO() {
		
 	}
    
    
	public LinhaResumoDTO(Long viagemId, LocalDate data, String rota,LocalDateTime saida,
			              LocalDateTime retorno, Integer kmPercorridos, String tipoDiaria, BigDecimal valorDiaria) {
		this.viagemId = viagemId;
		this.data = data;
		this.rota = rota;
		this.saida = saida;
		this.retorno = retorno;
		this.kmPercorridos = kmPercorridos;
		this.tipoDiaria = tipoDiaria;
		this.valorDiaria = valorDiaria;
	}


	public Long getViagemId() {
		return viagemId;
	}


	public LocalDate getData() {
		return data;
	}


	public String getRota() {
		return rota;
	}


	public String getTipoDiaria() {
		return tipoDiaria;
	}


	public BigDecimal getValorDiaria() {
		return valorDiaria;
	}


	public LocalDateTime getSaida() {
		return saida;
	}


	public LocalDateTime getRetorno() {
		return retorno;
	}


	public Integer getKmPercorridos() {
		return kmPercorridos;
	}

	
       

}
