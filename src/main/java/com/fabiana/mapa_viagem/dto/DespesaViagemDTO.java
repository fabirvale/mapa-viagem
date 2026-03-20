package com.fabiana.mapa_viagem.dto;

import com.fabiana.mapa_viagem.enums.TipoUnidadeDespesa;
import com.fabiana.mapa_viagem.model.DespesaViagem;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class DespesaViagemDTO extends OcorrenciaDuranteViagemDTO {
   		
	@NotNull(message = "Unidade é obrigatória")
    private TipoUnidadeDespesa unidade;

    @NotNull(message = "Quantidade é obrigatória")
    @Min(value = 1, message = "Quantidade deve ser maior que zero")
    private Integer quantidade;

    @Size(max = 50, message = "Documento fiscal deve ter no máximo 50 caracteres")
    private String documentoFiscal;

    @NotNull(message = "Km é obrigatório")
    @Min(value = 0, message = "Km não pode ser negativo")
    private Integer km;
	
	protected DespesaViagemDTO() {
		
	}

	public DespesaViagemDTO(DespesaViagem entity, Long viagemId) {
	    super(
	        entity != null ? entity.getId() : null,
	        entity != null ? entity.getDescricao() : null,
	        entity != null ? entity.getData() : null,
	        entity != null ? entity.getValor() : null,
	        viagemId
	    );

	    if (entity != null) {
	        this.unidade = entity.getUnidade();
	        this.quantidade = entity.getQuantidade();
	        this.documentoFiscal = entity.getDocumentoFiscal();
	        this.km = entity.getKm();
	    }
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

	

}
