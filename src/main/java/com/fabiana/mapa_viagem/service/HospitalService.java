package com.fabiana.mapa_viagem.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fabiana.mapa_viagem.dto.HospitalDTO;
import com.fabiana.mapa_viagem.exception.RecursoNaoEncontradoException;
import com.fabiana.mapa_viagem.model.Hospital;
import com.fabiana.mapa_viagem.repository.HospitalRepository;

import jakarta.transaction.Transactional;

@Service
public class HospitalService {
	
	@Autowired
	private HospitalRepository hospitalRepository;
	
	public List<HospitalDTO> findAll() {
		List<Hospital> list = hospitalRepository.findAll();
        List<HospitalDTO> listDto = new ArrayList<>();

        for (Hospital v : list) {
            listDto.add(new HospitalDTO(v));
        }

        return listDto;
	}
	
	public HospitalDTO findById(Long id) {
	    return hospitalRepository.findById(id).map(HospitalDTO::new).orElseThrow(() -> new RecursoNaoEncontradoException("Hospital não encontrado"));
	}
	
	 public HospitalDTO insert(HospitalDTO dto) { 
		 Hospital entity = fromDTO(dto);
		  entity = hospitalRepository.save(entity);
		  return new HospitalDTO(entity);
		      
	 }
	 
	 public void delete(Long id) {
		 hospitalRepository.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Hospital não encontrado"));
		 hospitalRepository.deleteById(id);
	}
	 
	 @Transactional
	 public HospitalDTO update(Long id, HospitalDTO dto) {

		 Hospital hospital = hospitalRepository.findById(id)
	         .orElseThrow(() -> new RecursoNaoEncontradoException("Hospital não encontrado"));

		 hospital.setNome(dto.getNome());
		 hospital.setEndereco(dto.getEndereco());
	     hospital.setNumero(dto.getNumero());
	     hospital.setComplemento(dto.getComplemento());
	     hospital.setBairro(dto.getBairro());
	     hospital.setCep(dto.getCep());
	     hospital.setCidade(dto.getCidade());

	     return new HospitalDTO(hospital);
	 }
 	 
	 private Hospital fromDTO(HospitalDTO objDto) {
			return new Hospital(objDto.getNome(),objDto.getEndereco(),objDto.getNumero(),objDto.getComplemento(), objDto.getBairro(), 
					            objDto.getCep(), objDto.getCidade(), objDto.getTipo());
	}
	
}
