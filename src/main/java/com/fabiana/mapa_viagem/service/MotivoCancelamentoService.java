package com.fabiana.mapa_viagem.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fabiana.mapa_viagem.dto.MotivoCancelamentoDTO;
import com.fabiana.mapa_viagem.exception.RecursoNaoEncontradoException;
import com.fabiana.mapa_viagem.model.MotivoCancelamento;
import com.fabiana.mapa_viagem.repository.MotivoCancelamentoRepository;

@Service
public class MotivoCancelamentoService {
	
	@Autowired
	private MotivoCancelamentoRepository motivoCancelamentoRepository;
	
	public List<MotivoCancelamentoDTO> findAll() {
		List<MotivoCancelamento> list = motivoCancelamentoRepository.findAll();
		List<MotivoCancelamentoDTO> listDto = new ArrayList<>();

	        for (MotivoCancelamento mc : list) {
	            listDto.add(new MotivoCancelamentoDTO(mc));
	        }

	        return listDto;
	}
	
	public MotivoCancelamentoDTO findById(Long id) {
		 return motivoCancelamentoRepository.findById(id).map(MotivoCancelamentoDTO::new).orElseThrow(() -> new RecursoNaoEncontradoException("Motivo do cancelamento não encontrado."));
	}
	

}
