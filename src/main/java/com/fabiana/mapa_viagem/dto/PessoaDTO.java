package com.fabiana.mapa_viagem.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class PessoaDTO {
		
    private Long id;
	
    @NotBlank
    private String nome;

    @NotNull
    private LocalDate dataNascimento;

    @NotBlank
    private String cpf;

    @NotBlank
    @Size(min = 5, max = 150)
    private String endereco;

    @NotBlank
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
