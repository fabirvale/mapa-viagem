package com.fabiana.mapa_viagem.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

@Entity
public class Acompanhante extends Pessoa {
		
	private String cns;
	
	public Acompanhante(Long id, String nome, LocalDate dataNascimento, String endereco, String telefone, String cns) {
		super(id, nome, dataNascimento, endereco, telefone);
		this.cns = cns;
	}

	public boolean validarCNS() {
		return false;
	}

}
