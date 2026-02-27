package com.fabiana.mapa_viagem.model;

import java.time.LocalDate;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public class Pessoa {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	private String nome;
	private LocalDate dataNascimento;
	private String cpf;
	private String endereco;
	private String telefone;
	
	
	public Pessoa() {
	
	}

	public Pessoa(Long id, String nome, LocalDate dataNascimento, String cpf, String endereco, String telefone) {
		this.id = id;
		this.nome = nome;
		this.dataNascimento = dataNascimento;
		this.cpf = cpf;
		this.endereco = endereco;
		this.telefone = telefone;
	}
	
	public Pessoa(String nome, LocalDate dataNascimento, String cpf, String endereco, String telefone) {
		this.nome = nome;
		this.dataNascimento = dataNascimento;
		this.cpf = cpf;
		this.endereco = endereco;
		this.telefone = telefone;
	}

	public Long getId() {
		return id;
	}

	
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public LocalDate getDataNascimento() {
		return dataNascimento;
	}
    
	public void setDataNascimento(LocalDate dataNascimento) {
		this.dataNascimento = dataNascimento;
	}
	
	public String getCpf() {
		return cpf;
	}
    
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	
	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}
	
	public int calcularIdade() {
		return 0;
	}
	
	public boolean validarCPF() {
		return false;
	}
	
	

}
