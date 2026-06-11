package com.fabiana.mapa_viagem.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import com.fabiana.mapa_viagem.exception.RegraNegocioException;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
public class Viagem {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	 private Long id;
	
	 private String observacao;
	 private String cidadeOrigem;
	 private String cidadeDestino;
	 private String estadoOrigem;
	 private String estadoDestino;
	 private LocalDate dataViagem;
	 private LocalTime horaPrevista;
	 private Integer odometroInicial;
	 private LocalDate dataRetorno;
	 private LocalTime horaChegada;
	 private Integer odometroFinal;
	 
	 @ManyToOne
	 @JoinColumn(name = "motorista_id")
	 private Motorista motorista;
	 
	 @ManyToOne
	 @JoinColumn(name = "veiculo_id")
	 private Veiculo veiculo;
	 
	 @OneToOne(cascade = CascadeType.ALL)
	 @JoinColumn(name = "pagamento_id")
	 private PagamentoDiaria pagamentoDiaria;
	 
	 //listas inicializadas na declaração
	 
	 @OneToMany(cascade = CascadeType.ALL)
	 @JoinColumn(name = "viagem_id")
	 List<Agendamento> agendamentos = new ArrayList<>();
	 
	 // Lista de ocorrências
	 @OneToMany
	 @JoinColumn(name = "viagem_id") // cria a coluna viagem_id nas tabelas de Ocorrência
	 List<OcorrenciaDuranteViagem> ocorrencias = new ArrayList<>();
	 
	 public Viagem () {
		 
	 }
	 
	 public Viagem(LocalDate dataViagem, String observacao, String cidadeOrigem, String cidadeDestino, String estadoOrigem, String estadoDestino, LocalTime horaPrevista, Motorista motorista, Veiculo veiculo) {
		         this.dataViagem = dataViagem;
		         this.observacao = observacao;
		         this.cidadeOrigem = cidadeOrigem;
			     this.cidadeDestino = cidadeDestino;
			     this.estadoOrigem = estadoOrigem;
			     this.estadoDestino = estadoDestino;
			     this.horaPrevista = horaPrevista;
			     this.motorista = motorista;
				 this.veiculo = veiculo;     
			}
	 
	
	 public Viagem(Long id, String observacao, String cidadeOrigem, String cidadeDestino,String estadoOrigem, String estadoDestino, LocalDate dataViagem,
			LocalTime horaPrevista, Integer odometroInicial, LocalDate dataRetorno, LocalTime horaChegada, Integer odometroFinal,
			Motorista motorista, Veiculo veiculo, List<Agendamento> agendamentos,
			List<OcorrenciaDuranteViagem> ocorrencias, PagamentoDiaria pagamentoDiaria) {
		this.id = id;
		this.observacao = observacao;
		this.cidadeOrigem = cidadeOrigem;
		this.cidadeDestino = cidadeDestino;
		this.estadoOrigem = estadoOrigem;
	    this.estadoDestino = estadoDestino;
		this.dataViagem = dataViagem;
		this.horaPrevista = horaPrevista;
		this.odometroInicial = odometroInicial;
		this.dataRetorno = dataRetorno;
		this.horaChegada = horaChegada;
		this.odometroFinal = odometroFinal;
		this.motorista = motorista;
		this.veiculo = veiculo;
		this.agendamentos = agendamentos;
		this.ocorrencias = ocorrencias;
		this.pagamentoDiaria = pagamentoDiaria;
	 }

	 public Long getId() {
		 return id;
	 }

	 public String getObservacao() {
		 return observacao;
	 }

	 public void setObservacao(String observacao) {
		 this.observacao = observacao;
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
	 
	 
	 public String getEstadoOrigem() {
		return estadoOrigem;
	}

	 public void setEstadoOrigem(String estadoOrigem) {
		 this.estadoOrigem = estadoOrigem;
	 }

	 public String getEstadoDestino() {
		 return estadoDestino;
	 }

	 public void setEstadoDestino(String estadoDestino) {
		 this.estadoDestino = estadoDestino;
	 }

	 public LocalDate getDataViagem() {
		 return dataViagem;
	 }

	 public void setDataViagem(LocalDate dataViagem) {
		 this.dataViagem = dataViagem;
	 }
	
	 public LocalTime getHoraPrevista() {
		 return horaPrevista;
	 }
     
	 public void setHoraPrevista(LocalTime horaPrevista) {
		 this.horaPrevista = horaPrevista;
	 }
	
	 public Integer getOdometroInicial() {
		 return odometroInicial;
	 }

	
	 public void setOdometroInicial(Integer odometroInicial) {
		this.odometroInicial = odometroInicial;
	}

	 public void setDataRetorno(LocalDate dataRetorno) {
		 this.dataRetorno = dataRetorno;
	 }

	 public void setHoraChegada(LocalTime horaChegada) {
		 this.horaChegada = horaChegada;
	 }
	 
	 public Integer getOdometroFinal() {
		 return odometroFinal;
	 }

	 public void setOdometroFinal(Integer odometroFinal) {
		 this.odometroFinal = odometroFinal;
	 }

	 public LocalDate getDataRetorno() {
		 return dataRetorno;
	 }

	
	 public LocalTime getHoraChegada() {
		 return horaChegada;
	 }

		
	 public Motorista getMotorista() {
		 return motorista;
	 }

	 public void setMotorista(Motorista motorista) {
		 this.motorista = motorista;
	 }

	 public Veiculo getVeiculo() {
		 return veiculo;
	 }

	 public void setVeiculo(Veiculo veiculo) {
		 this.veiculo = veiculo;
	 }

	 public List<Agendamento> getAgendamentos() {
		 return agendamentos;
	 }

	 public List<OcorrenciaDuranteViagem> getOcorrencias() {
		 return ocorrencias;
	 }

	
	 public PagamentoDiaria getPagamentoDiaria() {
		 return pagamentoDiaria;
	 }
	 
	 public void setPagamentoDiaria(PagamentoDiaria pagamentoDiaria) {
		 this.pagamentoDiaria = pagamentoDiaria;
	 }


		 
	 public void iniciarViagem() {

		    if (motorista == null) {
		        throw new IllegalStateException();
		    }

		    if (agendamentos == null || agendamentos.isEmpty()) {
		        throw new IllegalStateException();
		    }

		    boolean possuiAgendamentoNaData = false;

		    for (Agendamento agendamento : agendamentos) {
		        if (agendamento.getDataAtendimento().equals(dataViagem)) {
		            possuiAgendamentoNaData = true;
		            break;
		        }
		    }

		    if (!possuiAgendamentoNaData) {
		        throw new IllegalStateException();
		    }

		    // viagem iniciada
		}

/*	 public void finalizarViagem(int kmFinal, LocalDate dataRetorno, LocalTime horaChegada) {

		    // validações básicas
		    if (dataViagem == null || kmInicial <= 0) {
		        throw new IllegalStateException();
		    }

		    if (kmFinal < kmInicial) {
		        throw new IllegalStateException();
		    }

		    if (dataRetorno.isBefore(dataViagem)) {
		        throw new IllegalStateException();
		    }

		    if (pagamentoDiaria == null) {
		        throw new IllegalStateException();
		    }

		    // mudança de estado
		    this.kmFinal = kmFinal;
		    this.dataRetorno = dataRetorno;
		    this.horaChegada = horaChegada;

		    // regra de negócio derivada
		    pagamentoDiaria.definirTipoDiaria();
		}

	 
	 public Integer calcularKmPercorrido(){
		 if (kmFinal <= 0) {
		        return 0;
		    }
		 return kmFinal - kmInicial;
	 }*/
	 
     public BigDecimal calcularTotalOcorrencias() {
    	 
    	 BigDecimal totalOcorrencias = BigDecimal.ZERO;
    	 
    	 if (ocorrencias != null) {
    		 
    		 for (OcorrenciaDuranteViagem o :ocorrencias) {
    			
    			 totalOcorrencias = totalOcorrencias.add(o.calcularValor());
    		 }
    		 
    	 }
    	 
    	 return totalOcorrencias;
     }
     
    public BigDecimal calcularTotalDespesas() {
    	
       BigDecimal totalDespesas = BigDecimal.ZERO;
    	 
        if (ocorrencias != null) {
    		 
    		 for (OcorrenciaDuranteViagem o :ocorrencias) {
    			 if (o instanceof DespesaViagem) {
    				 totalDespesas = totalDespesas.add(o.calcularValor());
    			 }
    		 }
    	 }
    	 return totalDespesas;
     }
     
     public BigDecimal calcularTotalMultas() {
    	 
    	 BigDecimal totalMultas = BigDecimal.ZERO;
    	 
         if (ocorrencias != null) {
     		 
     		 for (OcorrenciaDuranteViagem o :ocorrencias) {
     			 if (o instanceof MultaViagem) {
     				totalMultas = totalMultas.add(o.calcularValor());
     			 }
     		 }
     	 }
     	 return totalMultas;
    	 
     }
     
     public void atualizarDataViagem(LocalDate novaData) {
    	    if (novaData == null) {
    	        throw new RegraNegocioException("A data da viagem não pode ser nula");
    	    }
    	    if (novaData.isBefore(LocalDate.now())) {
    	        throw new RegraNegocioException("A data da viagem não pode ser no passado");
    	    }

    	    this.dataViagem = novaData;
    	}

}
