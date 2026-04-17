package com.fabiana.mapa_viagem.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fabiana.mapa_viagem.dto.AgendamentoDTO;
import com.fabiana.mapa_viagem.exception.RecursoNaoEncontradoException;
import com.fabiana.mapa_viagem.exception.RegraNegocioException;
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

import jakarta.transaction.Transactional;

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
	
	@Autowired
	private ViagemService viagemService;
	
	
	public List<AgendamentoDTO> findAll(){
		List<Agendamento> list = agendamentoRepository.findAll();
		List<AgendamentoDTO> listDto = new ArrayList<>();	          

        for (Agendamento agenda : list) {
            listDto.add(new AgendamentoDTO(agenda));
        }

        return listDto;
      }
	
	public List<AgendamentoDTO> listarPorViagem(Long viagemId){
		List<Agendamento> list = agendamentoRepository.findByViagemId(viagemId);
		List<AgendamentoDTO> listDto = new ArrayList<>();	          

        for (Agendamento agenda : list) {
            listDto.add(new AgendamentoDTO(agenda));
        }

        return listDto;
      }
	
	
	public AgendamentoDTO findById(Long id) {
	 	
	 return agendamentoRepository.findById(id).map(AgendamentoDTO::new).orElseThrow(() -> new RecursoNaoEncontradoException("Agendamento não encontrado"));
		
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
		
		validarAgendamento(agendamentoDto, paciente, hospital, viagem);
		
		 Agendamento entity = fromDTO(agendamentoDto, paciente, acompanhante, hospital, viagem);
		 entity = agendamentoRepository.save(entity);
		 return new AgendamentoDTO(entity);
	}
	
	 public void delete(Long id) {
	    Agendamento agendamento = agendamentoRepository.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Agendamento não encontrado"));
  	   //Verificando se a viagem foi iniciada  
	    if (viagemService.viagemIniciada(agendamento.getViagem().getId())) 
  	    	throw new RegraNegocioException("A viagem já foi iniciada, não é permitido deletar.");   
	    agendamentoRepository.deleteById(id);	        
	}
	 
	 @Transactional
	 public AgendamentoDTO update(Long id, AgendamentoDTO dto) {
		 Agendamento agendamento = agendamentoRepository.findById(id)
	         .orElseThrow(() -> new RecursoNaoEncontradoException("Agendamento não encontrado"));

		//Buscar Viagem
		Viagem viagem =  viagemRepository.findById(dto.getViagemId()).orElseThrow(() -> new RecursoNaoEncontradoException("Viagem não encontrada"));
			
		//Verificar se a viagem já foi iniciada
		if (viagemService.viagemIniciada(viagem.getId())) {
		        throw new RegraNegocioException("A viagem já foi iniciada, não é permitido alterar.");
		 }
		
		//Buscar Paciente
		Paciente paciente =  pacienteRepository.findById(dto.getPacienteId()).orElseThrow(() -> new RecursoNaoEncontradoException("paciente não encontrado"));
			
		//Buscar Acompanhante
		Acompanhante acompanhante =  acompanhanteRepository.findById(dto.getAcompanhanteId()).orElseThrow(() -> new RecursoNaoEncontradoException("Acompanhante não encontrado"));
			
		//Buscar Hospital
		Hospital hospital =  hospitalRepository.findById(dto.getHospitalId()).orElseThrow(() -> new RecursoNaoEncontradoException("Hospital não encontrado"));
		
		//Validar regras de negocio
		validarAgendamento(dto, paciente, hospital, viagem);
		
		 agendamento.setPaciente(paciente);
		 agendamento.setAcompanhante(acompanhante);
		 agendamento.setHospital(hospital);
		 agendamento.setViagem(viagem);
		 agendamento.setDataAtendimento(dto.getDataAtendimento());
		 agendamento.setHorarioAtendimento(dto.getHorarioAtendimento());
		
	     return new AgendamentoDTO(agendamento);
	 }
	
	 private Agendamento fromDTO(AgendamentoDTO objDto, Paciente paciente, Acompanhante acompanhante, Hospital hospital, Viagem viagem) {
			return new Agendamento(paciente, acompanhante, hospital, viagem, objDto.getDataAtendimento(),objDto.getHorarioAtendimento());
			
			
	}
	 
	//validar regras antes de inserir ou alterar 
	public void validarAgendamento (AgendamentoDTO objDto, Paciente paciente, Hospital hospital, Viagem viagem) {
		
		if (!objDto.getDataAtendimento().equals(viagem.getDataViagem())) {
			 throw new RegraNegocioException("Data do atendimento diferente da data de viagem.");
		}
		
		if (!hospital.getCidade().trim().equalsIgnoreCase(viagem.getCidadeDestino().trim())) {
		  throw new RegraNegocioException("A Cidade destino é diferente da cidade onde está localizado o hospital.");
		}
		
		if (objDto.getHorarioAtendimento().isBefore(viagem.getHoraPrevista())) {
		 throw new RegraNegocioException("Horario de antedimento anterior a hora prevista da viagem.");	
		}
					   
	    // Verifica duplicidade de agendamento
        Optional<Agendamento> agendamentoExistente = agendamentoRepository.findByPacienteIdAndViagemId(paciente.getId(), viagem.getId());

        if (agendamentoExistente.isPresent() &&
        	    (objDto.getId() == null || !agendamentoExistente.get().getId().equals(objDto.getId()))) {
        	    throw new RegraNegocioException("Paciente já possui agendamento nesta viagem.");
        }
	}

}
