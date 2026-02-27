package com.fabiana.mapa_viagem.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;

@Entity
public class Motorista extends Pessoa{
	
	public Motorista() {
			super();
	}

	public Motorista(String nome, LocalDate dataNascimento, String cpf, String endereco, String telefone) {
		super(nome, dataNascimento, cpf, endereco, telefone);
	}

	
}
