package com.fabiana.mapa_viagem.dto;

import java.time.LocalDate;

public class PessoaDTO {
		
    private Long id;
	private String nome;
	private LocalDate dataNascimento;
	private String cpf;
	private String endereco;
	private String telefone;
	
	public PessoaDTO() {
		
	}

	public PessoaDTO(Long id, String nome, LocalDate dataNascimento, String cpf, String endereco, String telefone) {
		this.id = id;
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

	
	public String getCpf() {
		return cpf;
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
	
	
	
	

}
