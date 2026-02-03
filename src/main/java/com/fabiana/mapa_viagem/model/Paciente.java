package com.fabiana.mapa_viagem.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;

@Entity
public class Paciente extends Pessoa {
	private String cns;

	public Paciente(Long id, String nome, LocalDate dataNascimento, String endereco, String telefone, String cns) {
		super(id, nome, dataNascimento, endereco, telefone);
		this.cns = cns;
	}

	public boolean validarCNS() {
		return false;
	}
	
	

}
