package com.fabiana.mapa_viagem.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fabiana.mapa_viagem.dto.DespesaViagemDTO;
import com.fabiana.mapa_viagem.dto.OcorrenciaDuranteViagemDTO;
import com.fabiana.mapa_viagem.enums.TipoUnidadeDespesa;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "despesas_viagem")
@PrimaryKeyJoinColumn(name = "id")
public class DespesaViagem extends OcorrenciaDuranteViagem {
    
	@Enumerated(EnumType.STRING)
	private TipoUnidadeDespesa unidade;
	
	private Integer quantidade;
	private String documentoFiscal;
	private int km;
	
	protected DespesaViagem() {
		
	}

	public DespesaViagem(Long id, String descricao, LocalDate data, BigDecimal valor, TipoUnidadeDespesa unidade,
			Integer quantidade, String documentoFiscal, int km) {
		super(id, descricao, data, valor);
		this.unidade = unidade;
		this.quantidade = quantidade;
		this.documentoFiscal = documentoFiscal;
		this.km = km;
	}
	

	public TipoUnidadeDespesa getUnidade() {
		return unidade;
	}

	public void setUnidade(TipoUnidadeDespesa unidade) {
		this.unidade = unidade;
	}

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

	public String getDocumentoFiscal() {
		return documentoFiscal;
	}

	public void setDocumentoFiscal(String documentoFiscal) {
		this.documentoFiscal = documentoFiscal;
	}

	public int getKm() {
		return km;
	}

	public void setKm(int km) {
		this.km = km;
	}

	@Override
	public BigDecimal calcularValor() {

		if (quantidade != null && super.getValor() != null && quantidade > 0
				&& super.getValor().compareTo(BigDecimal.ZERO) > 0) {

			return super.getValor().multiply(BigDecimal.valueOf(quantidade));
		}

		return BigDecimal.ZERO;
	}
	
	@Override
	public OcorrenciaDuranteViagemDTO toDTO(Long viagemId) {
	    return new DespesaViagemDTO(this, viagemId);
	}

}
