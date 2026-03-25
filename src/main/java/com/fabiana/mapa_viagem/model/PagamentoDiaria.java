package com.fabiana.mapa_viagem.model;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

import com.fabiana.mapa_viagem.enums.TipoDiaria;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "pagamento_diaria")
public class PagamentoDiaria {
    
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Enumerated(EnumType.STRING)
	private TipoDiaria tipoDiaria;
	
	private BigDecimal valorDiaria;
	private LocalDateTime dataHoraSaida;
	private LocalDateTime dataHoraAlmoco;
	private LocalDateTime dataHoraRetorno;
	
	protected PagamentoDiaria() {
		
	}
		
	public PagamentoDiaria(Long id, TipoDiaria tipoDiaria, LocalDateTime dataHoraSaida,
			LocalDateTime dataHoraAlmoco, LocalDateTime dataHoraRetorno) {
		this.id = id;
		this.tipoDiaria = tipoDiaria;
		this.dataHoraSaida = dataHoraSaida;
		this.dataHoraAlmoco = dataHoraAlmoco;
		this.dataHoraRetorno = dataHoraRetorno;
	}

	
	

	public Long getId() {
		return id;
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

	public Duration calcularDuracaoViagem() {
		return Duration.between(dataHoraSaida, dataHoraRetorno);
	}
	
	public boolean possuiPernoite() {
	    return dataHoraRetorno.toLocalDate().isAfter(dataHoraSaida.toLocalDate());
	}

	public void definirTipoDiaria() {
	    long horas = calcularDuracaoViagem().toHours();

	    if (possuiPernoite()) {
	        this.tipoDiaria = TipoDiaria.COMPLETA_COM_PERNOITE;
	    } else {
	        for (TipoDiaria td : TipoDiaria.values()) {
	            if (td.aplicaPara((int) horas)) {
	                this.tipoDiaria = td;
	                break;
	            }
	        }
	    }
	}

}
