package com.fabiana.mapa_viagem.model;

import java.time.LocalDate;
import java.time.LocalTime;

import com.fabiana.mapa_viagem.exception.RegraNegocioException;

import jakarta.persistence.Entity;
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
			
	protected Agendamento() {
		
	}

	public Agendamento(Paciente paciente, Acompanhante acompanhante, Hospital hospital, Viagem viagem,
			LocalDate dataAtendimento, LocalTime horarioAtendimento) {
		this.paciente = paciente;
		this.acompanhante = acompanhante;
		validarAcompanhanteObrigatorio(); // garante regra de integridade (obrigatorio ter acompanhante)
		this.hospital = hospital;
		this.viagem = viagem;
		this.dataAtendimento = dataAtendimento;
		this.horarioAtendimento = horarioAtendimento;
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
