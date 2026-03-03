package com.fabiana.mapa_viagem.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fabiana.mapa_viagem.dto.AcompanhanteDTO;
import com.fabiana.mapa_viagem.exception.RecursoNaoEncontradoException;
import com.fabiana.mapa_viagem.model.Acompanhante;
import com.fabiana.mapa_viagem.repository.AcompanhanteRepository;

import jakarta.transaction.Transactional;

@Service
public class AcompanhanteService {

	@Autowired
	private AcompanhanteRepository acompanhanteRepository;
	
	public List<AcompanhanteDTO> findAll(){
		List<Acompanhante> acompanhantes = acompanhanteRepository.findAll();
		List<AcompanhanteDTO> listDto = new ArrayList<>();
		for (Acompanhante m: acompanhantes) {
		 listDto.add(new AcompanhanteDTO(m));	
		}
		return listDto;
	}
	
	public AcompanhanteDTO findById(Long id) {
		 return acompanhanteRepository.findById(id).map(AcompanhanteDTO::new).orElseThrow(() -> new RecursoNaoEncontradoException("acompanhante não encontrado"));
	}
	
	public AcompanhanteDTO insert(AcompanhanteDTO dto ) {
		  Acompanhante entity = fromDTO(dto);
		  entity = acompanhanteRepository.save(entity);
		  return new AcompanhanteDTO(entity);
	}
	
	 public void delete(Long id) {
		 acompanhanteRepository.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("acompanhante não encontrado"));
		 acompanhanteRepository.deleteById(id);
	}
	 
	 @Transactional
	 public AcompanhanteDTO update(Long id, AcompanhanteDTO dto) {

	     Acompanhante acompanhante = acompanhanteRepository.findById(id)
	         .orElseThrow(() -> new RecursoNaoEncontradoException("acompanhante não encontrado"));

	     acompanhante.setNome(dto.getNome());
	     acompanhante.setDataNascimento(dto.getDataNascimento());
	     acompanhante.setCpf(dto.getCpf());
	     acompanhante.setEndereco(dto.getEndereco());
	     acompanhante.setTelefone(dto.getTelefone());
	     return new AcompanhanteDTO(acompanhante);
	 }
 	 
	
	 private Acompanhante fromDTO(AcompanhanteDTO objDto) {
			return new Acompanhante(objDto.getNome(), objDto.getDataNascimento(), objDto.getCpf(), 
					objDto.getEndereco(), objDto.getTelefone(),objDto.getCns());
	}
	
	
}
