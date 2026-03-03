package com.fabiana.mapa_viagem.dto;

import com.fabiana.mapa_viagem.model.Paciente;

import jakarta.validation.constraints.NotBlank;

public class PacienteDTO extends PessoaDTO {
	@NotBlank
	private String cns;

	public PacienteDTO() {
		super();
		
	}

	public PacienteDTO(Paciente paciente) {
        super(paciente.getId(), paciente.getNome(), paciente.getDataNascimento(),
        		paciente.getCpf(), paciente.getEndereco(), paciente.getTelefone());
        this.cns = paciente.getCns();
    }
	
	
	public String getCns() {
		return cns;
	}

	public void setCns(String cns) {
		this.cns = cns;
	}


}
