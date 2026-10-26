package com.fabiana.mapa_viagem.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fabiana.mapa_viagem.enums.TipoDiaria;
import com.fabiana.mapa_viagem.model.PagamentoDiaria;

public class PagamentoDiariaDTO {
    private TipoDiaria tipoDiaria;
    private BigDecimal valorDiaria;
    private LocalDateTime dataHoraSaida;
    private LocalDateTime dataHoraAlmocoInicio;   
    private LocalDateTime dataHoraAlmocoFim;      
    private String localAlmoco;                  
    private LocalDateTime dataHoraRetorno;

    public PagamentoDiariaDTO(PagamentoDiaria entity) {
        if (entity != null) {
            this.tipoDiaria = entity.getTipoDiaria();
            this.valorDiaria = entity.getValorDiaria();
            this.dataHoraSaida = entity.getDataHoraSaida();
            this.dataHoraAlmocoInicio = entity.getDataHoraAlmocoInicio();
            this.dataHoraAlmocoFim = entity.getDataHoraAlmocoFim();
            this.localAlmoco = entity.getLocalAlmoco();
            this.dataHoraRetorno = entity.getDataHoraRetorno();
        }
    }

	public TipoDiaria getTipoDiaria() {
		return tipoDiaria;
	}

	public void setTipoDiaria(TipoDiaria tipoDiaria) {
		this.tipoDiaria = tipoDiaria;
	}

	public BigDecimal getValorDiaria() {
		return valorDiaria;
	}

	public void setValorDiaria(BigDecimal valorDiaria) {
		this.valorDiaria = valorDiaria;
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