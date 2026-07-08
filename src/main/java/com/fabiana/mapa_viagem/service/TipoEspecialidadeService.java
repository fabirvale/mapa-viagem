package com.fabiana.mapa_viagem.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fabiana.mapa_viagem.dto.TipoEspecialidadeDTO;
import com.fabiana.mapa_viagem.exception.RecursoNaoEncontradoException;
import com.fabiana.mapa_viagem.model.TipoEspecialidade;
import com.fabiana.mapa_viagem.repository.TipoEspecialidadeRepository;

@Service
public class TipoEspecialidadeService {

	@Autowired
	private TipoEspecialidadeRepository tipoEspecialidadeRepository;
	
	public List<TipoEspecialidadeDTO> findAll(){
		List<TipoEspecialidade> list = tipoEspecialidadeRepository.findAll();
        List<TipoEspecialidadeDTO> listDto = new ArrayList<>();

        for (TipoEspecialidade tc : list) {
            listDto.add(new TipoEspecialidadeDTO(tc));
        }

        return listDto;
	}
	
	public TipoEspecialidadeDTO findById(Long id) {
		 return tipoEspecialidadeRepository.findById(id).map(TipoEspecialidadeDTO::new).orElseThrow(() -> new RecursoNaoEncontradoException("Tipo de especialidade não encontrada"));
	}
	
	
	
}
