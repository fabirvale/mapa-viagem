package com.fabiana.mapa_viagem.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fabiana.mapa_viagem.dto.VeiculoDTO;
import com.fabiana.mapa_viagem.exception.RecursoNaoEncontradoException;
import com.fabiana.mapa_viagem.model.Veiculo;
import com.fabiana.mapa_viagem.repository.VeiculoRepository;

@Service
public class VeiculoService {
	
	@Autowired
	private VeiculoRepository veiculoRepository;
	
	public List<VeiculoDTO> findAll() {
		List<Veiculo> list = veiculoRepository.findAll();
        List<VeiculoDTO> listDto = new ArrayList<>();

        for (Veiculo v : list) {
            listDto.add(new VeiculoDTO(v));
        }

        return listDto;
	}
	
	public VeiculoDTO findById(Long id) {
	    return veiculoRepository.findById(id).map(VeiculoDTO::new).orElseThrow(() -> new RecursoNaoEncontradoException("Veículo não encontrado"));
	}
	

}
