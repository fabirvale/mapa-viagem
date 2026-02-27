package com.fabiana.mapa_viagem.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fabiana.mapa_viagem.dto.VeiculoDTO;
import com.fabiana.mapa_viagem.exception.RecursoNaoEncontradoException;
import com.fabiana.mapa_viagem.model.Veiculo;
import com.fabiana.mapa_viagem.repository.VeiculoRepository;

import jakarta.transaction.Transactional;

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
	
	 public VeiculoDTO insert(VeiculoDTO dto) { 
		  Veiculo entity = fromDTO(dto);
		  entity = veiculoRepository.save(entity);
		  return new VeiculoDTO(entity);
		      
	 }
	 
	 public void delete(Long id) {
		 veiculoRepository.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Veículo não encontrado"));
		 veiculoRepository.deleteById(id);
	}
	 
	 @Transactional
	 public VeiculoDTO update(Long id, VeiculoDTO dto) {

	     Veiculo veiculo = veiculoRepository.findById(id)
	         .orElseThrow(() -> new RecursoNaoEncontradoException("Veículo não encontrado"));

	     veiculo.setPlaca(dto.getPlaca());
	     veiculo.setModelo(dto.getModelo());
	     veiculo.setCapacidadePassageiros(dto.getCapacidadePassageiros());

	     return new VeiculoDTO(veiculo);
	 }
 	 
	 private Veiculo fromDTO(VeiculoDTO objDto) {
			return new Veiculo(objDto.getPlaca(),objDto.getModelo(),objDto.getCapacidadePassageiros());
	}
	

}
