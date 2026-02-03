package com.fabiana.mapa_viagem.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fabiana.mapa_viagem.dto.ViagemDTO;
import com.fabiana.mapa_viagem.model.Viagem;
import com.fabiana.mapa_viagem.repository.ViagemRepository;

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
    
	public Optional<ViagemDTO> findById(Long id) {
	    return viagemRepository.findById(id).map(ViagemDTO::new);
	}
	
		
	 public ViagemDTO insert(ViagemDTO dto) {
	        Viagem entity = fromDTO(dto);
	        entity = viagemRepository.save(entity);
	        return new ViagemDTO(entity);
	    }
	
	public Viagem fromDTO(ViagemDTO objDto) {
		return new Viagem(objDto.getDataViagem(),objDto.getCidadeOrigem(),objDto.getCidadeDestino());
	}


}
