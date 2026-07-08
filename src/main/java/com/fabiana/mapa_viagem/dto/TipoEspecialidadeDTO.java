package com.fabiana.mapa_viagem.dto;

import com.fabiana.mapa_viagem.model.TipoEspecialidade;

public class TipoEspecialidadeDTO {
	
    private Long id;  
	private String especialidade;
    
    public TipoEspecialidadeDTO() {
    	
	}

    public TipoEspecialidadeDTO(TipoEspecialidade tipoEspecialidade) {
		this.id   = tipoEspecialidade.getId();
		this.especialidade = tipoEspecialidade.getEspecialidade();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public String getEspecialidade() {
		return especialidade;
	}


	public void setEspecialidade(String especialidade) {
		this.especialidade = especialidade;
	}


}
