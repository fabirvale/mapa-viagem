package com.fabiana.mapa_viagem.dto;

import com.fabiana.mapa_viagem.model.Acompanhante;

public class AcompanhanteDTO extends PessoaDTO{
	
	private String cns;

	public AcompanhanteDTO() {
		super();
	}

	// Apenas mapeando id, nome e cpf
    public AcompanhanteDTO(Acompanhante acompanhante) {
        super(acompanhante.getId(), acompanhante.getNome(), null, acompanhante.getCpf(), null, null);
        this.cns = acompanhante.getCns();
    }

	public String getCns() {
		return cns;
	}

}
