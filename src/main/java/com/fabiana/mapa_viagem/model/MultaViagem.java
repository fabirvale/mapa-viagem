package com.fabiana.mapa_viagem.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Entity;

@Entity
public class MultaViagem extends OcorrenciaDuranteViagem {

	private String autoInfracao;
	private LocalTime horario;
	private String municipio;
	private String descricaoMulta;

	public MultaViagem(Long id, String descricao, LocalDate data, BigDecimal valor, String autoInfracao,
			LocalTime horario, String municipio, String descricaoMulta) {
		super(id, descricao, data, valor);
		this.autoInfracao = autoInfracao;
		this.horario = horario;
		this.municipio = municipio;
		this.descricaoMulta = descricaoMulta;
	}

	public String getAutoInfracao() {
		return autoInfracao;
	}

	public LocalTime getHorario() {
		return horario;
	}

	public String getMunicipio() {
		return municipio;
	}

	public String getDescricaoMulta() {
		return descricaoMulta;
	}

	@Override
	public BigDecimal calcularValor() {

		if (super.getValor() != null && super.getValor().compareTo(BigDecimal.ZERO) > 0) {

			return super.getValor();
		}

		return BigDecimal.ZERO;
	}

}
