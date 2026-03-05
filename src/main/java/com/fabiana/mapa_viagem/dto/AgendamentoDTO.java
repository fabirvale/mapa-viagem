package com.fabiana.mapa_viagem.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import com.fabiana.mapa_viagem.model.Agendamento;

import jakarta.validation.constraints.NotNull;

public class AgendamentoDTO {
	private Long id;
	
	@NotNull(message = "Paciente é obrigatório")
	private Long pacienteId;
	
	@NotNull(message = "Acompanhante é obrigatória")
	private Long acompanhanteId;
	
	@NotNull(message = "Hospital é obrigatório")
	private Long hospitalId;
		
	@NotNull(message = "Viagem é obrigatória")
	private Long viagemId;
	
	@NotNull(message = "Data do atendimento é obrigatória")
	private LocalDate dataAtendimento;
	
	@NotNull(message = "Horário do atendimento é obrigatório")
	private LocalTime horarioAtendimento;
	
	private String pacienteNome;
	private String acompanhanteNome;
	private String hospitalNome;
	private LocalDate viagemData;

	public AgendamentoDTO() {
		
	}
	
	public AgendamentoDTO(Agendamento obj) {
		this.id = obj.getId();
	    this.pacienteId = obj.getPaciente().getId();
	    this.pacienteNome = obj.getPaciente().getNome(); // pegando o nome do paciente
	    this.acompanhanteId = obj.getAcompanhante().getId();
	    this.acompanhanteNome = obj.getAcompanhante().getNome(); // nome do acompanhante
	    this.hospitalId = obj.getHospital().getId();
	    this.hospitalNome = obj.getHospital().getNome(); // nome do hospital
	    this.viagemId = obj.getViagem().getId();
	    this.viagemData = obj.getViagem().getDataViagem(); // data da viagem
	    this.dataAtendimento = obj.getDataAtendimento();
	    this.horarioAtendimento = obj.getHorarioAtendimento();
	}


	public Long getId() {
		return id;
	}


	public Long getPacienteId() {
		return pacienteId;
	}

	public void setPacienteId(Long pacienteId) {
		this.pacienteId = pacienteId;
	}

	public Long getAcompanhanteId() {
		return acompanhanteId;
	}

	public void setAcompanhanteId(Long acompanhanteId) {
		this.acompanhanteId = acompanhanteId;
	}

	public Long getHospitalId() {
		return hospitalId;
	}

	public void setHospitalId(Long hospitalId) {
		this.hospitalId = hospitalId;
	}

	public Long getViagemId() {
		return viagemId;
	}

	public void setViagemId(Long viagemId) {
		this.viagemId = viagemId;
	}

	public LocalDate getDataAtendimento() {
		return dataAtendimento;
	}

	public void setDataAtendimento(LocalDate dataAtendimento) {
		this.dataAtendimento = dataAtendimento;
	}

	public LocalTime getHorarioAtendimento() {
		return horarioAtendimento;
	}

	public void setHorarioAtendimento(LocalTime horarioAtendimento) {
		this.horarioAtendimento = horarioAtendimento;
	}

	public String getPacienteNome() {
		return pacienteNome;
	}

	public String getAcompanhanteNome() {
		return acompanhanteNome;
	}

	public String getHospitalNome() {
		return hospitalNome;
	}

	public LocalDate getViagemData() {
		return viagemData;
	}
	
    
}
