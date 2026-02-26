package com.fabiana.mapa_viagem.dto;

import com.fabiana.mapa_viagem.model.Paciente;

public class PacienteDTO extends PessoaDTO {
	
	private String cns;

	public PacienteDTO() {
		super();
	}

	// Apenas mapeando id, nome e cpf
    public PacienteDTO(Paciente paciente) {
        super(paciente.getId(), paciente.getNome(), null, paciente.getCpf(), null, null);
        this.cns = paciente.getCns();
    }

	public String getCns() {
		return cns;
	}

}
