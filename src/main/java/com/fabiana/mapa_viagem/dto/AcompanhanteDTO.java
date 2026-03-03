package com.fabiana.mapa_viagem.dto;

import com.fabiana.mapa_viagem.model.Acompanhante;

import jakarta.validation.constraints.NotBlank;

public class AcompanhanteDTO extends PessoaDTO{
	@NotBlank
	private String cns;

	public AcompanhanteDTO() {
		super();
	}

	// Apenas mapeando id, nome e cpf
    public AcompanhanteDTO(Acompanhante acompanhante) {
        super(acompanhante.getId(), acompanhante.getNome(), acompanhante.getDataNascimento(),
        	  acompanhante.getCpf(), acompanhante.getEndereco(), acompanhante.getTelefone());
        this.cns = acompanhante.getCns();
    }
   
	public String getCns() {
		return cns;
	}

}
