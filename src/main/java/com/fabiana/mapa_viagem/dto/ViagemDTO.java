package com.fabiana.mapa_viagem.dto;

import java.time.LocalDate;

import com.fabiana.mapa_viagem.model.Viagem;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ViagemDTO {
	
	private Long id;
	@Size(max = 255, message = "Descrição deve ter no máximo 255 caracteres")
	private String descricao;
	@NotBlank(message = "Cidade de origem é obrigatória")
    private String cidadeOrigem;
	@NotBlank(message = "Cidade de destino é obrigatória")
    private String cidadeDestino;
	@NotNull(message = "Data da viagem é obrigatória")
    private LocalDate dataViagem;

    // construtor vazio (obrigatório para Jackson)
    public ViagemDTO() {
    }

    // construtor que converte Entity -> DTO
    public ViagemDTO(Viagem entity) {
        this.id = entity.getId();
        this.descricao = entity.getDescricao();
        this.cidadeOrigem = entity.getCidadeOrigem();
        this.cidadeDestino = entity.getCidadeDestino();
        this.dataViagem = entity.getDataViagem();
    }

    // getters e setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    
    public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getCidadeOrigem() {
        return cidadeOrigem;
    }

    public void setCidadeOrigem(String cidadeOrigem) {
        this.cidadeOrigem = cidadeOrigem;
    }

    public String getCidadeDestino() {
        return cidadeDestino;
    }

    public void setCidadeDestino(String cidadeDestino) {
        this.cidadeDestino = cidadeDestino;
    }

    public LocalDate getDataViagem() {
        return dataViagem;
    }

    public void setDataViagem(LocalDate dataViagem) {
        this.dataViagem = dataViagem;
    }

}
