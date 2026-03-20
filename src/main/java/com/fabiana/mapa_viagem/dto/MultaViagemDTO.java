package com.fabiana.mapa_viagem.dto;

import java.time.LocalTime;

import com.fabiana.mapa_viagem.model.MultaViagem;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class MultaViagemDTO extends OcorrenciaDuranteViagemDTO {

	 @NotBlank(message = "Auto de infração é obrigatório")
	 @Size(max = 30, message = "Auto de infração deve ter no máximo 30 caracteres")
	 private String autoInfracao;

	 @NotNull(message = "Horário é obrigatório")
	 private LocalTime horario;
	 
	 @NotBlank(message = "Municipio é obrigatório")
	 private String municipio;
	 
	 @NotBlank(message = "Descrição da multa é obrigatório")
	 @Size(max = 255, message = "Descrição da multa deve ter no máximo 255 caracteres")
	 private String descricaoMulta;
	
	protected MultaViagemDTO() {
		
	}

	 public MultaViagemDTO(MultaViagem entity, Long viagemId) {      
	        super(
	    	        entity != null ? entity.getId() : null,
	    	        entity != null ? entity.getDescricao() : null,
	    	        entity != null ? entity.getData() : null,
	    	        entity != null ? entity.getValor() : null,
	    	        viagemId
	    	    );

	    	    if (entity != null) {
	    	    	 this.autoInfracao = entity.getAutoInfracao();
	    		     this.horario = entity.getHorario();
	    		     this.municipio = entity.getMunicipio();
	    		     this.descricaoMulta = entity.getDescricaoMulta();
	    	    }
	        
	    }

	public String getAutoInfracao() {
		return autoInfracao;
	}

	public void setAutoInfracao(String autoInfracao) {
		this.autoInfracao = autoInfracao;
	}

	public LocalTime getHorario() {
		return horario;
	}

	public void setHorario(LocalTime horario) {
		this.horario = horario;
	}

	public String getMunicipio() {
		return municipio;
	}

	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}

	public String getDescricaoMulta() {
		return descricaoMulta;
	}

	public void setDescricaoMulta(String descricaoMulta) {
		this.descricaoMulta = descricaoMulta;
	}

	

	
}
