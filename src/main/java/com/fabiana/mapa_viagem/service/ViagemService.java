package com.fabiana.mapa_viagem.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fabiana.mapa_viagem.dto.ViagemDTO;
import com.fabiana.mapa_viagem.exception.RecursoNaoEncontradoException;
import com.fabiana.mapa_viagem.exception.RegraNegocioException;
import com.fabiana.mapa_viagem.model.Viagem;
import com.fabiana.mapa_viagem.repository.ViagemRepository;

import jakarta.transaction.Transactional;

//registrado como componente
@Service
public class ViagemService {
	
	@Autowired
	private ViagemRepository viagemRepository;
		
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

	    if (dto.getDataViagem() != null) {
	        viagem.atualizarDataViagem(dto.getDataViagem());
	    }

	}

	private Viagem fromDTO(ViagemDTO objDto) {
		return new Viagem(objDto.getDataViagem(),objDto.getDescricao(), objDto.getCidadeOrigem(),objDto.getCidadeDestino());
	}


}
