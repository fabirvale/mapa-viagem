package com.fabiana.mapa_viagem.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fabiana.mapa_viagem.dto.AgendamentoDTO;
import com.fabiana.mapa_viagem.exception.RecursoNaoEncontradoException;
import com.fabiana.mapa_viagem.model.Acompanhante;
import com.fabiana.mapa_viagem.model.Agendamento;
import com.fabiana.mapa_viagem.model.Hospital;
import com.fabiana.mapa_viagem.model.Paciente;
import com.fabiana.mapa_viagem.model.Viagem;
import com.fabiana.mapa_viagem.repository.AcompanhanteRepository;
import com.fabiana.mapa_viagem.repository.AgendamentoRepository;
import com.fabiana.mapa_viagem.repository.HospitalRepository;
import com.fabiana.mapa_viagem.repository.PacienteRepository;
import com.fabiana.mapa_viagem.repository.ViagemRepository;

@Service
public class AgendamentoService {
	
	@Autowired
	private AgendamentoRepository agendamentoRepository;
	
	@Autowired
	private PacienteRepository pacienteRepository;
	
	@Autowired
	private AcompanhanteRepository acompanhanteRepository;
	
	@Autowired
	private HospitalRepository hospitalRepository;
	
	@Autowired
	private ViagemRepository viagemRepository;
	
	
	public List<AgendamentoDTO> findAll(){
		List<Agendamento> list = agendamentoRepository.findAll();
		List<AgendamentoDTO> listDto = new ArrayList<>();	          

        for (Agendamento agenda : list) {
            listDto.add(new AgendamentoDTO(agenda));
        }

        return listDto;
      }
	
	public AgendamentoDTO insert (AgendamentoDTO agendamentoDto) {
		
		//Buscar Paciente
		Paciente paciente =  pacienteRepository.findById(agendamentoDto.getPacienteId()).orElseThrow(() -> new RecursoNaoEncontradoException("paciente não encontrado"));
		
		//Buscar Acompanhante
		Acompanhante acompanhante =  acompanhanteRepository.findById(agendamentoDto.getAcompanhanteId()).orElseThrow(() -> new RecursoNaoEncontradoException("Acompanhante não encontrado"));
		
		//Buscar Hospital
		Hospital hospital =  hospitalRepository.findById(agendamentoDto.getHospitalId()).orElseThrow(() -> new RecursoNaoEncontradoException("Hospital não encontrado"));

		//Buscar Viagem
		Viagem viagem =  viagemRepository.findById(agendamentoDto.getViagemId()).orElseThrow(() -> new RecursoNaoEncontradoException("Viagem não encontrada"));
		
		 Agendamento entity = fromDTO(agendamentoDto, paciente, acompanhante, hospital, viagem);
		 entity = agendamentoRepository.save(entity);
		 return new AgendamentoDTO(entity);
	}
	
	 private Agendamento fromDTO(AgendamentoDTO objDto, Paciente paciente, Acompanhante acompanhante, Hospital hospital, Viagem viagem) {
			return new Agendamento(paciente, acompanhante, hospital, viagem, objDto.getDataAtendimento(),objDto.getHorarioAtendimento());
			
			
	}
	

}
