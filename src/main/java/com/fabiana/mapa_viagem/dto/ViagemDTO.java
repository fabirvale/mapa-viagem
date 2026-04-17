package com.fabiana.mapa_viagem.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import com.fabiana.mapa_viagem.model.DespesaViagem;
import com.fabiana.mapa_viagem.model.MultaViagem;
import com.fabiana.mapa_viagem.model.OcorrenciaDuranteViagem;
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
	private Integer kmInicial;
	private LocalDate dataRetorno;
	private LocalTime horaChegada;
	private Integer kmFinal;
	private List<OcorrenciaDuranteViagemDTO> ocorrencias;
	private PagamentoDiariaDTO pagamentoDiaria;
	private String status;
	

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
        
     // chama o método privado para popular a lista de ocorrências
        this.ocorrencias = new ArrayList<>();
        adicionarOcorrencias(entity.getOcorrencias(), entity.getId());
        
        // Pagamento diária (se existir)
        if (entity.getPagamentoDiaria() != null) {
            this.pagamentoDiaria = new PagamentoDiariaDTO(entity.getPagamentoDiaria());
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

   public Integer getKmInicial() {
	return kmInicial;
   }

   public void setKmInicial(Integer kmInicial) {
	this.kmInicial = kmInicial;
   }

   public LocalDate getDataRetorno() {
	return dataRetorno;
   }

   public void setDataRetorno(LocalDate dataRetorno) {
	this.dataRetorno = dataRetorno;
   }

   public LocalTime getHoraChegada() {
	return horaChegada;
   }

   public void setHoraChegada(LocalTime horaChegada) {
	this.horaChegada = horaChegada;
   }

   public Integer getKmFinal() {
	return kmFinal;
   }

   public void setKmFinal(Integer kmFinal) {
	this.kmFinal = kmFinal;
   }
   
  
  public PagamentoDiariaDTO getPagamentoDiaria() {
	return pagamentoDiaria;
}

   public void setPagamentoDiaria(PagamentoDiariaDTO pagamentoDiaria) {
	this.pagamentoDiaria = pagamentoDiaria;
   }
   
   public String getStatus() {
	    return status;
	}

	public void setStatus(String status) {
	    this.status = status;
	}

  // Método privado para adicionar ocorrências no DTO
  private void adicionarOcorrencias(List<OcorrenciaDuranteViagem> ocorrenciasEntity, Long viagemId) {
	    if (ocorrenciasEntity != null && !ocorrenciasEntity.isEmpty()) {
	        for (OcorrenciaDuranteViagem o : ocorrenciasEntity) {
	            if (o instanceof DespesaViagem) {
	                this.ocorrencias.add(new DespesaViagemDTO((DespesaViagem) o, viagemId));
	            } else if (o instanceof MultaViagem) {
	                this.ocorrencias.add(new MultaViagemDTO((MultaViagem) o, viagemId));
	            }
	        }
	    }
	}
     
  }
