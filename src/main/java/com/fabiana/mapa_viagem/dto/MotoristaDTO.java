package com.fabiana.mapa_viagem.dto;

import com.fabiana.mapa_viagem.model.Motorista;

public class MotoristaDTO extends PessoaDTO{

	public MotoristaDTO() {
		super();
	}

	
    public MotoristaDTO(Motorista motorista) {
        super(motorista.getId(), motorista.getNome(), motorista.getDataNascimento(),
        	  motorista.getCpf(), motorista.getEndereco(), motorista.getTelefone());
    }


}
