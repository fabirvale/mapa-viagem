package com.fabiana.mapa_viagem.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import com.fabiana.mapa_viagem.enums.TipoCompromisso;
import com.fabiana.mapa_viagem.model.Agendamento;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class AgendamentoDTO {
	private Long id;
	
	@NotNull(message = "Paciente é obrigatório")
	private Long pacienteId;
	
	@NotNull(message = "Acompanhante é obrigatória")
	private Long acompanhanteId;
	
	@NotNull(message = "Hospital é obrigatório")
	private Long hospitalId;
	
	@NotNull(message = "Tipo de especialidade é obrigatório")
	private Long tipoEspecialidade_Id;
		
	@NotNull(message = "Viagem é obrigatória")
	private Long viagemId;
	
	@NotNull(message = "Data do atendimento é obrigatória")
	private LocalDate dataAtendimento;
	
	@NotNull(message = "Horário do atendimento é obrigatório")
	private LocalTime horarioAtendimento;
	
	private String pacienteNome;
	private String acompanhanteNome;
	private String hospitalNome;
	private String especialidade;
	private LocalDate viagemData;
	
	@NotNull(message = "Tipo de Compromisso é obrigatório")
	private TipoCompromisso tipoCompromisso;
		
	private Boolean cadeirante;
	private Boolean maca;
	private Boolean oxigenio;
	private Boolean outrosCuidados;
	
	@Size(max = 255, message = "Observação deve ter no máximo 255 caracteres")
	@Column(length = 255)
	private String observacao;
	
	private Boolean ida;
	private Boolean volta;

	public AgendamentoDTO() {
		
	}
	
	public AgendamentoDTO(Agendamento obj) {
		this.id = obj.getId();
	    this.pacienteId = obj.getPaciente().getId();
	    this.pacienteNome = obj.getPaciente().getNome(); // pegando o nome do paciente
	    this.acompanhanteId = obj.getAcompanhante().getId();
	    this.acompanhanteNome = obj.getAcompanhante().getNome(); // nome do acompanhante
	    this.hospitalId = obj.getHospital().getId();
	    this.tipoEspecialidade_Id = obj.getTipoEspecialidade().getId();
	    this.hospitalNome = obj.getHospital().getNome(); // nome do hospital
	    this.especialidade = obj.getTipoEspecialidade().getEspecialidade();
	    this.viagemId = obj.getViagem().getId();
	    this.viagemData = obj.getViagem().getDataViagem(); // data da viagem
	    this.dataAtendimento = obj.getDataAtendimento();
	    this.horarioAtendimento = obj.getHorarioAtendimento();
	    this.tipoCompromisso = obj.getTipoCompromisso();
	    this.cadeirante = obj.getCadeirante();
	    this.maca = obj.getMaca();
	    this.oxigenio = obj.getOxigenio();
	    this.outrosCuidados = obj.getOutrosCuidados();
	    this.observacao = obj.getObservacao();
	    this.ida = obj.getIda();
	    this.volta = obj.getVolta();
	    
	    
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

	public TipoCompromisso getTipoCompromisso() {
		return tipoCompromisso;
	}

	public void setTipoCompromisso(TipoCompromisso tipoCompromisso) {
		this.tipoCompromisso = tipoCompromisso;
	}
	
	public Long getTipoEspecialidade_Id() {
		return tipoEspecialidade_Id;
	}

	public void setTipoEspecialidade_Id(Long tipoEspecialidade_Id) {
		this.tipoEspecialidade_Id = tipoEspecialidade_Id;
	}

	public String getEspecialidade() {
		return especialidade;
	}

	public void setEspecialidade(String especialidade) {
		this.especialidade = especialidade;
	}

	public Boolean getCadeirante() {
		return cadeirante;
	}

	public void setCadeirante(Boolean cadeirante) {
		this.cadeirante = cadeirante;
	}

	public Boolean getMaca() {
		return maca;
	}

	public void setMaca(Boolean maca) {
		this.maca = maca;
	}

	public Boolean getOxigenio() {
		return oxigenio;
	}

	public void setOxigenio(Boolean oxigenio) {
		this.oxigenio = oxigenio;
	}

	public Boolean getOutrosCuidados() {
		return outrosCuidados;
	}

	public void setOutrosCuidados(Boolean outrosCuidados) {
		this.outrosCuidados = outrosCuidados;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public Boolean getIda() {
		return ida;
	}

	public void setIda(Boolean ida) {
		this.ida = ida;
	}

	public Boolean getVolta() {
		return volta;
	}

	public void setVolta(Boolean volta) {
		this.volta = volta;
	}
	
    
}
