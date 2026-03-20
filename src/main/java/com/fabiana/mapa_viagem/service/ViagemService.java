package com.fabiana.mapa_viagem.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fabiana.mapa_viagem.dto.ViagemDTO;
import com.fabiana.mapa_viagem.exception.RecursoNaoEncontradoException;
import com.fabiana.mapa_viagem.exception.RegraNegocioException;
import com.fabiana.mapa_viagem.model.Motorista;
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
            listDto.add(new ViagemDTO(v));
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

	 public void fecharViagem(Long viagemId, ViagemDTO dto) {

		    Viagem viagem = viagemRepository.findById(viagemId)
		            .orElseThrow(() -> new RecursoNaoEncontradoException("Viagem não encontrada"));

		    //Validação de data + hora
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
		}

}
