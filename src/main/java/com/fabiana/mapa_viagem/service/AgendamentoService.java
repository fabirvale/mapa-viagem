package com.fabiana.mapa_viagem.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fabiana.mapa_viagem.dto.AgendamentoDTO;
import com.fabiana.mapa_viagem.enums.StatusAgendamento;
import com.fabiana.mapa_viagem.enums.StatusViagem;
import com.fabiana.mapa_viagem.exception.RecursoNaoEncontradoException;
import com.fabiana.mapa_viagem.exception.RegraNegocioException;
import com.fabiana.mapa_viagem.model.Acompanhante;
import com.fabiana.mapa_viagem.model.Agendamento;
import com.fabiana.mapa_viagem.model.Hospital;
import com.fabiana.mapa_viagem.model.MotivoCancelamento;
import com.fabiana.mapa_viagem.model.Paciente;
import com.fabiana.mapa_viagem.model.TipoEspecialidade;
import com.fabiana.mapa_viagem.model.Viagem;
import com.fabiana.mapa_viagem.repository.AcompanhanteRepository;
import com.fabiana.mapa_viagem.repository.AgendamentoRepository;
import com.fabiana.mapa_viagem.repository.HospitalRepository;
import com.fabiana.mapa_viagem.repository.MotivoCancelamentoRepository;
import com.fabiana.mapa_viagem.repository.PacienteRepository;
import com.fabiana.mapa_viagem.repository.TipoEspecialidadeRepository;
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
	private TipoEspecialidadeRepository tipoEspecialidadeRepository;
	
	@Autowired
	private ViagemRepository viagemRepository;
	
	@Autowired
	private MotivoCancelamentoRepository motivoCancelamentoRepository;

	
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
  
	    //Buscar Tipo de Especialidade
		 TipoEspecialidade tipoEspecialidade =  tipoEspecialidadeRepository.findById(agendamentoDto.getTipoEspecialidade_Id()).orElseThrow(() -> new RecursoNaoEncontradoException("Tipo de especialidade não encontrado"));

		//Buscar Viagem
		Viagem viagem =  viagemRepository.findById(agendamentoDto.getViagemId()).orElseThrow(() -> new RecursoNaoEncontradoException("Viagem não encontrada"));
		validarStatusViagem(viagem);
		
		validarAgendamento(agendamentoDto, paciente, hospital, viagem);
		
		 Agendamento entity = fromDTO(agendamentoDto, paciente, acompanhante, hospital, tipoEspecialidade, viagem);
		 entity.setStatus(StatusAgendamento.AGENDADO);
		 entity = agendamentoRepository.save(entity);
		 return new AgendamentoDTO(entity);
	}
	
	 public void delete(Long id) {
	    Agendamento agendamento = agendamentoRepository.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Agendamento não encontrado"));
	    if (agendamento.getViagem().getStatus() != StatusViagem.AGENDADA) {

	        throw new RegraNegocioException("Somente agendamentos de viagens agendadas podem ser excluídos.");
	    }
	    agendamentoRepository.deleteById(id);	        
	}
	 
	 @Transactional
	 public AgendamentoDTO update(Long id, AgendamentoDTO dto) {
		 Agendamento agendamento = agendamentoRepository.findById(id)
	         .orElseThrow(() -> new RecursoNaoEncontradoException("Agendamento não encontrado"));

		//Buscar Viagem
		Viagem viagem =  viagemRepository.findById(dto.getViagemId()).orElseThrow(() -> new RecursoNaoEncontradoException("Viagem não encontrada"));
						
		//Buscar Paciente
		Paciente paciente =  pacienteRepository.findById(dto.getPacienteId()).orElseThrow(() -> new RecursoNaoEncontradoException("paciente não encontrado"));
			
		//Buscar Acompanhante
		Acompanhante acompanhante =  acompanhanteRepository.findById(dto.getAcompanhanteId()).orElseThrow(() -> new RecursoNaoEncontradoException("Acompanhante não encontrado"));
			
		//Buscar Hospital
		Hospital hospital =  hospitalRepository.findById(dto.getHospitalId()).orElseThrow(() -> new RecursoNaoEncontradoException("Hospital não encontrado"));
		
		//Buscar Especialidade
		TipoEspecialidade tipoEspecialidade =  tipoEspecialidadeRepository.findById(dto.getTipoEspecialidade_Id()).orElseThrow(() -> new RecursoNaoEncontradoException("Tipo de especialidade não encontrado"));
				
		
		//Validar regras de negocio
		validarAgendamento(dto, paciente, hospital, viagem);
		
		 agendamento.setPaciente(paciente);
		 agendamento.setAcompanhante(acompanhante);
		 agendamento.setIda(dto.getIda());
		 agendamento.setVolta(dto.getVolta());
		 agendamento.setHospital(hospital);
		 agendamento.setViagem(viagem);
		 agendamento.setDataAtendimento(dto.getDataAtendimento());
		 agendamento.setHorarioAtendimento(dto.getHorarioAtendimento());
		 agendamento.setTipoEspecialidade(tipoEspecialidade);
		 agendamento.setTipoCompromisso(dto.getTipoCompromisso());
		 agendamento.setObservacao(dto.getObservacao());
		 agendamento.setCadeirante(dto.getCadeirante());
		 agendamento.setMaca(dto.getMaca());
		 agendamento.setOxigenio(dto.getOxigenio());
		 agendamento.setOutrosCuidados(dto.getOutrosCuidados());
		 		
	     return new AgendamentoDTO(agendamento);
	 }
	 
	 //cancelar agendamento
	   public void cancelarAgendamento(Long id, AgendamentoDTO dto) {
	      Agendamento agendamento = agendamentoRepository.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Agendamento não encontrado"));
		
	      if (agendamento.getStatus() != StatusAgendamento.AGENDADO) {
			   throw new RegraNegocioException("Somente agendamentos com status agendados podem ser cancelados.");
		  }
	      
	      if (dto.getMotivoCancelamentoId() == null) {
	          throw new RegraNegocioException("O motivo do cancelamento é obrigatório.");
	      }    
	     
	     MotivoCancelamento motivo = motivoCancelamentoRepository.findById(dto.getMotivoCancelamentoId()).orElseThrow(() ->
	    	                         new RecursoNaoEncontradoException("Motivo de cancelamento não encontrado"));

	     agendamento.setStatus(StatusAgendamento.CANCELADO);
	     agendamento.setMotivoCancelamento(motivo);
	     agendamento.setObservacao(dto.getObservacao());
	     agendamentoRepository.save(agendamento);
	  }

	
	 
	 private Agendamento fromDTO(AgendamentoDTO objDto, Paciente paciente, Acompanhante acompanhante, Hospital hospital, TipoEspecialidade tipoEspecialidade, Viagem viagem) {
			return new Agendamento(paciente, acompanhante, hospital, tipoEspecialidade, viagem, objDto.getDataAtendimento(),objDto.getHorarioAtendimento(),
					              objDto.getTipoCompromisso(), objDto.getCadeirante(),
					              objDto.getMaca(), objDto.getOxigenio(), objDto.getOutrosCuidados(), objDto.getObservacao(),
					              objDto.getIda(), objDto.getVolta(), objDto.getStatus());
			
			
	}
	 
	private void validarStatusViagem(Viagem viagem) {

		    if (viagem.getStatus() != StatusViagem.AGENDADA) {
		        throw new RegraNegocioException(
		            "Não é possível criar um agendamento para uma viagem que não está agendada."
		        );
		    }
		}
	 
	//validar regras antes de inserir ou alterar 
	public void validarAgendamento (AgendamentoDTO objDto, Paciente paciente, Hospital hospital, Viagem viagem) {
		
		 LocalDateTime dataHoraViagem = LocalDateTime.of(viagem.getDataViagem(), viagem.getHoraPrevista());

		 if (LocalDateTime.now().isAfter(dataHoraViagem)) {
		     throw new RegraNegocioException("Não é possível alterar agendamentos de uma viagem cujo horário previsto já passou.");
		 }
		
		if (!objDto.getDataAtendimento().equals(viagem.getDataViagem())) {
			 throw new RegraNegocioException("Data do atendimento diferente da data de viagem.");
		}
		
		if (!hospital.getCidade().trim().equalsIgnoreCase(viagem.getCidadeDestino().trim())) {
		  throw new RegraNegocioException("A Cidade destino é diferente da cidade onde está localizado o hospital.");
		}
		
		
		if (!objDto.getHorarioAtendimento().isAfter(viagem.getHoraPrevista())) {

		    throw new RegraNegocioException("Horário do atendimento deve ser posterior ao horário previsto da viagem." );
		}
					   
	    // Verifica duplicidade de agendamento
        Optional<Agendamento> agendamentoExistente = agendamentoRepository.findByPacienteIdAndViagemId(paciente.getId(), viagem.getId());

        if (agendamentoExistente.isPresent() && (objDto.getId() == null || !agendamentoExistente.get().getId().equals(objDto.getId()))) {

            throw new RegraNegocioException( "Paciente já possui agendamento nesta viagem.");
        }
                
     // Verifica se o paciente já possui agendamento na mesma data
        Optional<Agendamento> agendamentoNaMesmaData = agendamentoRepository.findByPacienteIdAndDataAtendimento(paciente.getId(),
                                                        objDto.getDataAtendimento());

        if (agendamentoNaMesmaData.isPresent()) {

            boolean novoAgendamento = objDto.getId() == null;
            boolean outroAgendamento = !agendamentoNaMesmaData.get().getId().equals(objDto.getId());

            if (novoAgendamento || outroAgendamento) {
                throw new RegraNegocioException("Paciente já possui agendamento nesta data.");
            }
        }
       
	}

}
