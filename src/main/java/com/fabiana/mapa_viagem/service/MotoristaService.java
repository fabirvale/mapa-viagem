package com.fabiana.mapa_viagem.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fabiana.mapa_viagem.dto.MotoristaDTO;
import com.fabiana.mapa_viagem.exception.RecursoNaoEncontradoException;
import com.fabiana.mapa_viagem.model.Motorista;
import com.fabiana.mapa_viagem.repository.MotoristaRepository;

import jakarta.transaction.Transactional;

@Service
public class MotoristaService {

	@Autowired
	private MotoristaRepository motoristaRepository;
	
	public List<MotoristaDTO> findAll(){
		List<Motorista> motoristas = motoristaRepository.findAll();
		List<MotoristaDTO> listDto = new ArrayList<>();
		for (Motorista m: motoristas) {
		 listDto.add(new MotoristaDTO(m));	
		}
		return listDto;
	}
	
	public MotoristaDTO findById(Long id) {
		 return motoristaRepository.findById(id).map(MotoristaDTO::new).orElseThrow(() -> new RecursoNaoEncontradoException("Motorista não encontrado"));
	}
	
	public MotoristaDTO insert(MotoristaDTO dto ) {
		  Motorista entity = fromDTO(dto);
		  entity = motoristaRepository.save(entity);
		  return new MotoristaDTO(entity);
	}
	
	 public void delete(Long id) {
		 motoristaRepository.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Motorista não encontrado"));
		 motoristaRepository.deleteById(id);
	}
	 
	 @Transactional
	 public MotoristaDTO update(Long id, MotoristaDTO dto) {

	     Motorista motorista = motoristaRepository.findById(id)
	         .orElseThrow(() -> new RecursoNaoEncontradoException("Motorista não encontrado"));

	     motorista.setNome(dto.getNome());
	     motorista.setDataNascimento(dto.getDataNascimento());
	     motorista.setCpf(dto.getCpf());
	     motorista.setEndereco(dto.getEndereco());
	     motorista.setTelefone(dto.getTelefone());
	     return new MotoristaDTO(motorista);
	 }
 	 
	
	 private Motorista fromDTO(MotoristaDTO objDto) {
			return new Motorista(objDto.getNome(), objDto.getDataNascimento(), objDto.getCpf(), objDto.getEndereco(), objDto.getTelefone());
	}
	
	
}
