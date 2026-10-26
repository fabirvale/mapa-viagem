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
	private LocalDateTime dataHoraAlmocoInicio;
	private LocalDateTime dataHoraAlmocoFim;
	private String localAlmoco;
	private LocalDateTime dataHoraRetorno;

	public PagamentoDiaria() {

	}

	public PagamentoDiaria(Long id, TipoDiaria tipoDiaria, LocalDateTime dataHoraSaida,
			LocalDateTime dataHoraAlmocoInicio, LocalDateTime dataHoraAlmocoFim, String localAlmoco,
			LocalDateTime dataHoraRetorno) {
		this.id = id;
		this.tipoDiaria = tipoDiaria;
		this.dataHoraSaida = dataHoraSaida;
		this.dataHoraAlmocoInicio = dataHoraAlmocoInicio;
		this.dataHoraAlmocoFim = dataHoraAlmocoFim;
		this.localAlmoco = localAlmoco;
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

	// Duração trabalhada: da saída ao retorno, descontando o tempo de almoço
	public Duration calcularDuracaoViagem() {
		Duration total = Duration.between(dataHoraSaida, dataHoraRetorno);

		if (dataHoraAlmocoInicio != null && dataHoraAlmocoFim != null) {
			total = total.minus(Duration.between(dataHoraAlmocoInicio, dataHoraAlmocoFim));
		}
		return total;
	}

	public boolean possuiPernoite() {
		return dataHoraRetorno.toLocalDate().isAfter(dataHoraSaida.toLocalDate());
	}

	public void definirTipoDiaria(int kmPercorridos) {
	    long minutos = calcularDuracaoViagem().toMinutes();

	    // Regra do cliente: KM >= 300 ou virada de dia → pernoite
	    if (kmPercorridos >= 300 || possuiPernoite()) {
	        this.tipoDiaria = TipoDiaria.COMPLETA_COM_PERNOITE;
	    } else if (TipoDiaria.COMPLETA_SEM_PERNOITE.aplicaPara((int) minutos)) {
	        this.tipoDiaria = TipoDiaria.COMPLETA_SEM_PERNOITE;
	    } else if (TipoDiaria.PARCIAL_SEM_PERNOITE.aplicaPara((int) minutos)) {
	        this.tipoDiaria = TipoDiaria.PARCIAL_SEM_PERNOITE;
	    } else {
	        this.tipoDiaria = TipoDiaria.SEM_DIARIA;   // menos de 5h efetivas
	    }
	}

}
