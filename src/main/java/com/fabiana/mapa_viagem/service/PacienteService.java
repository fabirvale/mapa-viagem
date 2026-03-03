package com.fabiana.mapa_viagem.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fabiana.mapa_viagem.dto.PacienteDTO;
import com.fabiana.mapa_viagem.exception.RecursoNaoEncontradoException;
import com.fabiana.mapa_viagem.model.Paciente;
import com.fabiana.mapa_viagem.repository.PacienteRepository;

import jakarta.transaction.Transactional;

@Service
public class PacienteService {

	@Autowired
	private PacienteRepository pacienteRepository;
	
	public List<PacienteDTO> findAll(){
		List<Paciente> pacientes = pacienteRepository.findAll();
		List<PacienteDTO> listDto = new ArrayList<>();
		for (Paciente m: pacientes) {
		 listDto.add(new PacienteDTO(m));	
		}
		return listDto;
	}
	
	public PacienteDTO findById(Long id) {
		 return pacienteRepository.findById(id).map(PacienteDTO::new).orElseThrow(() -> new RecursoNaoEncontradoException("paciente não encontrado"));
	}
	
	public PacienteDTO insert(PacienteDTO dto ) {
		  Paciente entity = fromDTO(dto);
		  entity = pacienteRepository.save(entity);
		  return new PacienteDTO(entity);
	}
	
	 public void delete(Long id) {
		 pacienteRepository.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("paciente não encontrado"));
		 pacienteRepository.deleteById(id);
	}
	 
	 @Transactional
	 public PacienteDTO update(Long id, PacienteDTO dto) {

	     Paciente paciente = pacienteRepository.findById(id)
	         .orElseThrow(() -> new RecursoNaoEncontradoException("paciente não encontrado"));

	     paciente.setNome(dto.getNome());
	     paciente.setDataNascimento(dto.getDataNascimento());
	     paciente.setCpf(dto.getCpf());
	     paciente.setEndereco(dto.getEndereco());
	     paciente.setTelefone(dto.getTelefone());
	     return new PacienteDTO(paciente);
	 }
 	 
	
	 private Paciente fromDTO(PacienteDTO objDto) {
			return new Paciente(objDto.getNome(), objDto.getDataNascimento(), objDto.getCpf(), 
					objDto.getEndereco(), objDto.getTelefone(),objDto.getCns());
	}
	
	
}
