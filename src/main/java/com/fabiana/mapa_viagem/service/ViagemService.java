package com.fabiana.mapa_viagem.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fabiana.mapa_viagem.dto.FecharViagemDTO;
import com.fabiana.mapa_viagem.dto.ViagemDTO;
import com.fabiana.mapa_viagem.exception.RecursoNaoEncontradoException;
import com.fabiana.mapa_viagem.exception.RegraNegocioException;
import com.fabiana.mapa_viagem.model.Motorista;
import com.fabiana.mapa_viagem.model.OcorrenciaDuranteViagem;
import com.fabiana.mapa_viagem.model.PagamentoDiaria;
import com.fabiana.mapa_viagem.model.Veiculo;
import com.fabiana.mapa_viagem.model.Viagem;
import com.fabiana.mapa_viagem.repository.AgendamentoRepository;
import com.fabiana.mapa_viagem.repository.MotoristaRepository;
import com.fabiana.mapa_viagem.repository.VeiculoRepository;
import com.fabiana.mapa_viagem.repository.ViagemRepository;

import jakarta.transaction.Transactional;

//registrado como componente
@Service
public class ViagemService {
	
	@Autowired
	private ViagemRepository viagemRepository;
	
	@Autowired
    private AgendamentoRepository agendamentoRepository;
	
	@Autowired
    private MotoristaRepository motoristaRepository;
	
	@Autowired
    private VeiculoRepository veiculoRepository;
			
	public List<ViagemDTO> findAll() {
		List<Viagem> list = viagemRepository.findAll();
        List<ViagemDTO> listDto = new ArrayList<>();

        for (Viagem v : list) {
            ViagemDTO dto = new ViagemDTO(v);

            if (viagemFinalizada(v)) {
                dto.setStatus("FINALIZADA");

            } else if (viagemIniciada(v.getId())) {
                dto.setStatus("INICIADA");

            } else {
                dto.setStatus("AGENDADA");
            }

            listDto.add(dto); 
        }
        return listDto;
	}
    
	public ViagemDTO findById(Long id) {
	    Viagem entity = buscarViagemOuFalhar(id);
	    return new ViagemDTO(entity);
	}
	
	private Viagem buscarViagemOuFalhar(Long id) {
		    return viagemRepository.findById(id)
		            .orElseThrow(() -> new RecursoNaoEncontradoException("Viagem não encontrada"));
		} 
		
	 public ViagemDTO insert(ViagemDTO dto) { 
		// REGRA DE NEGÓCIO
		    if (dto.getCidadeOrigem().equalsIgnoreCase(dto.getCidadeDestino())) {
		        throw new RegraNegocioException("Cidade de origem não pode ser igual à cidade de destino");
		    }
	        Viagem entity = fromDTO(dto);
	        entity = viagemRepository.save(entity);
	         return new ViagemDTO(entity);
	      
	    }
	 
	 public void delete(Long id) {
		    Viagem viagem = buscarViagemOuFalhar(id);
		    viagemRepository.delete(viagem);
	}
	 
		
	// tudo ok commit, not ok rollback
	@Transactional 
	public void update(Long id, ViagemDTO dto) {

		Viagem viagem = buscarViagemOuFalhar(id);

	    if (dto.getCidadeOrigem() != null) {
	        viagem.setCidadeOrigem(dto.getCidadeOrigem());
	    }

	    if (dto.getCidadeDestino() != null) {
	        viagem.setCidadeDestino(dto.getCidadeDestino());
	    }
	    
	    if (dto.getHoraPrevista() != null) {
	        viagem.setHoraPrevista(dto.getHoraPrevista());
	    }

	    if (dto.getDataViagem() != null) {
	    	
	    	if (!dto.getDataViagem().equals(viagem.getDataViagem())) { 
	    	   boolean existeAgendamento = agendamentoRepository.existsByViagemId(id);
	    	   if (existeAgendamento) {
	                throw new RegraNegocioException("Não é possível alterar a data da viagem pois existem agendamentos vinculados.");
	            }                
	    	} 
	    	 viagem.atualizarDataViagem(dto.getDataViagem());
	    }

	}
	
	//EndPoint PATCH para o frontend adicionar/alterar motorista
	public void adicionarMotorista(Long viagemId, Long motoristaId) {
	    Viagem viagem = viagemRepository.findById(viagemId)
	        .orElseThrow(() -> new RegraNegocioException("Viagem não encontrada"));

	    Motorista motorista = motoristaRepository.findById(motoristaId)
	        .orElseThrow(() -> new RegraNegocioException("Motorista não encontrado"));

	    viagem.setMotorista(motorista);

	    viagemRepository.save(viagem);
	}
	
	//EndPoint PATCH para o frontend adicionar/alterar motorista
		public void adicionarVeiculo(Long viagemId, Long veiculoId) {
		    Viagem viagem = viagemRepository.findById(viagemId)
		        .orElseThrow(() -> new RegraNegocioException("Viagem não encontrada"));

		    Veiculo veiculo = veiculoRepository.findById(veiculoId)
		        .orElseThrow(() -> new RegraNegocioException("Veículo não encontrado"));

		    viagem.setVeiculo(veiculo);

		    viagemRepository.save(viagem);
		}

	private Viagem fromDTO(ViagemDTO objDto) {
		
		Motorista motorista = null;
	    Veiculo veiculo = null;

	    if (objDto.getMotoristaId() != null) {
	        motorista = motoristaRepository.findById(objDto.getMotoristaId())
	                .orElseThrow(() -> new RegraNegocioException("Motorista não encontrado."));
	    }

	    if (objDto.getVeiculoId() != null) {
	        veiculo = veiculoRepository.findById(objDto.getVeiculoId())
	                .orElseThrow(() -> new RegraNegocioException("Veículo não encontrado."));
	    }
		return new Viagem(objDto.getDataViagem(),objDto.getDescricao(), objDto.getCidadeOrigem(),
				  objDto.getCidadeDestino(), objDto.getHoraPrevista(), motorista, veiculo);
	}
	
	 public boolean viagemIniciada(Long viagemId) {
	       Viagem viagem = viagemRepository.findById(viagemId).orElseThrow(() -> new RecursoNaoEncontradoException("Viagem não encontrada"));

	     // Validação da data (se for futura, já retorna false)
	       if (viagem.getDataViagem().isAfter(LocalDate.now())) {
	            return false;
	        }
	        
	        // Verifica se existe pelo menos um agendamento
	        boolean temAgendamento = agendamentoRepository.existsByViagemId(viagemId);

	        // Verifica se há motorista e veículo
	        boolean temMotorista = viagem.getMotorista() != null;
	        boolean temVeiculo = viagem.getVeiculo() != null;

	        // Retorna true se todos os requisitos mínimos estão preenchidos
	        return temAgendamento && temMotorista && temVeiculo;
	    }

	 public void fecharViagem(Long viagemId, ViagemDTO dto, FecharViagemDTO fecharViagemDto) {

		    Viagem viagem = viagemRepository.findById(viagemId)
		            .orElseThrow(() -> new RecursoNaoEncontradoException("Viagem não encontrada"));
		    
		    //Calculando o valor total das Ocorrencias
		    BigDecimal totalOcorrencias = calcularTotalOcorrencias(viagem);

		    //Validação de data + hora da Viagem
		    LocalDateTime inicio = LocalDateTime.of(viagem.getDataViagem(), viagem.getHoraPrevista());
		    LocalDateTime chegada = LocalDateTime.of(dto.getDataRetorno(), dto.getHoraChegada());

		    if (chegada.isBefore(inicio)) {
		        throw new RegraNegocioException("A chegada não pode ser antes do início da viagem");
		    }

		    //Validação de KM
		    if (dto.getKmFinal() <= dto.getKmInicial()) {
		        throw new RegraNegocioException("Km final deve ser maior que o km inicial");
		    }

		    //Atualização
		    viagem.setDataRetorno(dto.getDataRetorno());
		    viagem.setHoraChegada(dto.getHoraChegada());
		    viagem.setKmInicial(dto.getKmInicial());
		    viagem.setKmFinal(dto.getKmFinal());
		    
		    // Cria pagamento da diária
		    PagamentoDiaria pagamento = new PagamentoDiaria();
		    pagamento.setDataHoraSaida(fecharViagemDto.getDataHoraSaida());
		    pagamento.setDataHoraAlmoco(fecharViagemDto.getDataHoraAlmoco());
		    pagamento.setDataHoraRetorno(fecharViagemDto.getDataHoraRetorno());

		 // Define tipo de diária considerando regra de horas, pernoite e KM
		    int kmPercorridos = dto.getKmFinal() - dto.getKmInicial();
		    pagamento.definirTipoDiaria(kmPercorridos);

		    // Define valor de acordo com tipo
		    pagamento.setValorDiaria(pagamento.getTipoDiaria().getValor());

		    // Associa à viagem
		    viagem.setPagamentoDiaria(pagamento);

		    // Salva a viagem com pagamento
		    viagemRepository.save(viagem);    
	}
	 
	 public boolean viagemFinalizada(Viagem viagem) {
		    return viagem.getDataRetorno() != null &&
		           viagem.getHoraChegada() != null &&
		           viagem.getKmFinal() != null &&
		           viagem.getPagamentoDiaria() != null;
		}
	 
	 //Calcular o total da ocorrencia
	 private BigDecimal calcularTotalOcorrencias(Viagem viagem) {

		    BigDecimal total = BigDecimal.ZERO;

		    List<OcorrenciaDuranteViagem> ocorrencias = viagem.getOcorrencias();

		    if (ocorrencias == null || ocorrencias.isEmpty()) {
		        return BigDecimal.ZERO;
		    }

		    for (OcorrenciaDuranteViagem o : ocorrencias) {
		        total = total.add(o.calcularValor());
		    }

		    return total;
		}

}
