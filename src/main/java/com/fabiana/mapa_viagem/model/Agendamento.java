package com.fabiana.mapa_viagem.model;

import java.time.LocalDate;
import java.time.LocalTime;

import com.fabiana.mapa_viagem.enums.TipoCompromisso;
import com.fabiana.mapa_viagem.exception.RegraNegocioException;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Agendamento {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "paciente_id")
	private Paciente paciente;
	
	@ManyToOne
	@JoinColumn(name = "acompanhante_id")
	private Acompanhante acompanhante;
	
	@ManyToOne
    @JoinColumn(name = "hospital_id")
	private Hospital hospital;
	
	@ManyToOne
    @JoinColumn(name = "viagem_id")
	private Viagem viagem;
	
	private LocalDate dataAtendimento;
	private LocalTime horarioAtendimento;
	
	@Enumerated(EnumType.STRING)
	private TipoCompromisso tipoCompromisso;
	
	@ManyToOne
	@JoinColumn(name = "tipoEspecialidade_id")
	private TipoEspecialidade tipoEspecialidade;
	
	private Boolean cadeirante;
	private Boolean maca;
	private Boolean oxigenio;
	private Boolean outrosCuidados;
	private String observacao;
	private Boolean ida;
	private Boolean volta;
			
	protected Agendamento() {
		
	}

	public Agendamento(Paciente paciente, Acompanhante acompanhante, Hospital hospital,TipoEspecialidade tipoEspecialidade, Viagem viagem,
			LocalDate dataAtendimento, LocalTime horarioAtendimento,TipoCompromisso tipoCompromisso,
			 Boolean cadeirante, Boolean maca, Boolean oxigenio,
			Boolean outrosCuidados, String observacao, Boolean ida, Boolean volta) {
		this.paciente = paciente;
		this.acompanhante = acompanhante;
		validarAcompanhanteObrigatorio(); // garante regra de integridade (obrigatorio ter acompanhante)
		this.hospital = hospital;
		this.viagem = viagem;
		this.dataAtendimento = dataAtendimento;
		this.horarioAtendimento = horarioAtendimento;
		this.tipoCompromisso = tipoCompromisso;
		this.tipoEspecialidade = tipoEspecialidade;
		this.cadeirante = cadeirante;
		this.maca = maca;
		this.oxigenio = oxigenio;
		this.outrosCuidados = outrosCuidados;
		this.observacao = observacao;
		this.ida = ida;
		this.volta = volta;
	}
	
	public Long getId() {
		return id;
	}

	
	public Paciente getPaciente() {
		return paciente;
	}

	
	public Acompanhante getAcompanhante() {
		return acompanhante;
	}


	public Hospital getHospital() {
		return hospital;
	}

	
	public Viagem getViagem() {
		return viagem;
	}

	
	public LocalDate getDataAtendimento() {
		return dataAtendimento;
	}

	
	public LocalTime getHorarioAtendimento() {
		return horarioAtendimento;
	}
	
	
	public void setPaciente(Paciente paciente) {
		this.paciente = paciente;
	}

	public void setAcompanhante(Acompanhante acompanhante) {
		this.acompanhante = acompanhante;
	}

	public void setHospital(Hospital hospital) {
		this.hospital = hospital;
	}

	public void setViagem(Viagem viagem) {
		this.viagem = viagem;
	}

	public void setDataAtendimento(LocalDate dataAtendimento) {
		this.dataAtendimento = dataAtendimento;
	}

	public void setHorarioAtendimento(LocalTime horarioAtendimento) {
		this.horarioAtendimento = horarioAtendimento;
	}
	
	public TipoCompromisso getTipoCompromisso() {
		return tipoCompromisso;
	}

	public void setTipoCompromisso(TipoCompromisso tipoCompromisso) {
		this.tipoCompromisso = tipoCompromisso;
	}

	public TipoEspecialidade getTipoEspecialidade() {
		return tipoEspecialidade;
	}

	public void setTipoEspecialidade(TipoEspecialidade tipoEspecialidade) {
		this.tipoEspecialidade = tipoEspecialidade;
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

	private void validarAcompanhanteObrigatorio() {
		if(acompanhante == null){
		    throw new RegraNegocioException("Acompanhante é obrigatório no agendamento");
		}
	}

			
	public void reagendar(LocalDate novaData, LocalTime novoHorario) {
		this.dataAtendimento = novaData;
		this.horarioAtendimento = novoHorario;
	}
	
	 public boolean pertenceAViagem() {
		 
		return viagem != null;
	 }
	
}
