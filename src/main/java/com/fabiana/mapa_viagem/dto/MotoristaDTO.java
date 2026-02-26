package com.fabiana.mapa_viagem.dto;

import com.fabiana.mapa_viagem.model.Motorista;

public class MotoristaDTO extends PessoaDTO{

	public MotoristaDTO() {
		super();
	}

	// Apenas mapeando id, nome e cpf
    public MotoristaDTO(Motorista motorista) {
        super(motorista.getId(), motorista.getNome(), null, motorista.getCpf(), null, null);
    }


}
