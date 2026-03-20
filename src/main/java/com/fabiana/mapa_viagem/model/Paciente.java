package com.fabiana.mapa_viagem.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;

@Entity
public class Paciente extends Pessoa {
	private String cns;
	
	protected Paciente() {
	}
	
	public Paciente(String nome, LocalDate dataNascimento, String cpf, String endereco, String telefone, String cns) {
		super(nome, dataNascimento, cpf, endereco, telefone);
		this.cns = cns;
	}

	
	public String getCns() {
		return cns;
	}


	public boolean validarCNS() {
		return false;
	}
	
	

}
