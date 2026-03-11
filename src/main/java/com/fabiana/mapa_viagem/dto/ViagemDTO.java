package com.fabiana.mapa_viagem.dto;

import java.time.LocalDate;
import java.time.LocalTime;

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
	@NotNull(message = "Hora prevista da viagem é obrigatória")
	private LocalTime horaPrevista;
	private Long motoristaId;
	private String motoristaNome;
	private Long veiculoId;
	private String veiculoModelo;
	

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
        this.horaPrevista = entity.getHoraPrevista();
        if (entity.getMotorista() != null) {
          this.motoristaId = entity.getMotorista().getId();
          this.motoristaNome = entity.getMotorista().getNome();
        }
        
        if (entity.getVeiculo() != null) {
          this.veiculoId = entity.getVeiculo().getId();
          this.veiculoModelo = entity.getVeiculo().getModelo();
        }
    }

    // getters e setters
    public Long getId() {
        return id;
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

    public String getCidadeDestino() {
        return cidadeDestino;
    }

   public LocalDate getDataViagem() {
        return dataViagem;
    }

   public LocalTime getHoraPrevista() {
	return horaPrevista;
   }

   public Long getMotoristaId() {
	return motoristaId;
   }

   public String getMotoristaNome() {
		return motoristaNome;
	   }

   public Long getVeiculoId() {
		return veiculoId;
   }
   public String getVeiculoModelo() {
	return veiculoModelo;
   }

     
  }
