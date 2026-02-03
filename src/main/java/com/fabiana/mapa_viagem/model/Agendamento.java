package com.fabiana.mapa_viagem.model;

import java.time.LocalDate;
import java.time.LocalTime;

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
	
	public Agendamento(Long id, Paciente paciente, Acompanhante acompanhante, Hospital hospital, Viagem viagem,
			LocalDate dataAtendimento, LocalTime horarioAtendimento) {
		this.id = id;
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

	
	private void validarAcompanhanteObrigatorio() {
	    if (acompanhante == null) {
	        throw new IllegalStateException();
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
