package com.fabiana.mapa_viagem.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fabiana.mapa_viagem.dto.DespesaViagemDTO;
import com.fabiana.mapa_viagem.dto.MultaViagemDTO;
import com.fabiana.mapa_viagem.dto.OcorrenciaDuranteViagemDTO;
import com.fabiana.mapa_viagem.exception.RecursoNaoEncontradoException;
import com.fabiana.mapa_viagem.exception.RegraNegocioException;
import com.fabiana.mapa_viagem.model.DespesaViagem;
import com.fabiana.mapa_viagem.model.MultaViagem;
import com.fabiana.mapa_viagem.model.OcorrenciaDuranteViagem;
import com.fabiana.mapa_viagem.model.Viagem;
import com.fabiana.mapa_viagem.repository.OcorrenciaDuranteViagemRepository;
import com.fabiana.mapa_viagem.repository.ViagemRepository;

@Service
public class OcorrenciaDuranteViagemService {
	
	@Autowired
	private OcorrenciaDuranteViagemRepository ocorrenciasRepository;
	
	@Autowired
	private ViagemRepository viagemRepository;
	
	public List<OcorrenciaDuranteViagemDTO> findAll() {
	    List<OcorrenciaDuranteViagem> list = ocorrenciasRepository.findAll();
	    List<OcorrenciaDuranteViagemDTO> listDto = new ArrayList<>();

	    for (OcorrenciaDuranteViagem ocorrencia : list) {
	    	 Long viagemId = ocorrencia.getViagem() != null ? ocorrencia.getViagem().getId() : null;
	    	 listDto.add(ocorrencia.toDTO(viagemId));
	    }

	    return listDto;
	}
	
	public OcorrenciaDuranteViagemDTO findById(Long id) {
	    OcorrenciaDuranteViagem ocorrencia = ocorrenciasRepository.findById(id)
	                                        .orElseThrow(() -> new RecursoNaoEncontradoException("Ocorrencia não encontrada"));
	    Long viagemId = ocorrencia.getViagem() != null ? ocorrencia.getViagem().getId() : null;
	    return ocorrencia.toDTO(viagemId);
	}
	
	public List<OcorrenciaDuranteViagemDTO> findViagemId(Long viagemId) {
	    List<OcorrenciaDuranteViagem> list = ocorrenciasRepository.findByViagemId(viagemId);
	    List<OcorrenciaDuranteViagemDTO> listDto = new ArrayList<>();

	    for (OcorrenciaDuranteViagem ocorrencia : list) {
	    	 Long idViagem = ocorrencia.getViagem() != null ? ocorrencia.getViagem().getId() : null;
	    	 listDto.add(ocorrencia.toDTO(idViagem));
	    }

	    return listDto;
	}
	
	public OcorrenciaDuranteViagemDTO insert(OcorrenciaDuranteViagemDTO dto) {
	    // Buscar a viagem
	    Viagem viagem = viagemRepository.findById(dto.getViagemId())
	            .orElseThrow(() -> new RecursoNaoEncontradoException("Viagem não encontrada"));
	    
	    if (viagem.getDataViagem() == null) {
	        throw new RegraNegocioException("Data de início da viagem não está preenchida");
	    }
	   
	    if (dto.getData().isBefore(viagem.getDataViagem())) {
	        throw new RegraNegocioException("Data da ocorrência não pode ser antes do início da viagem");
	    }

	    if (viagem.getDataRetorno() != null && 
	        dto.getData().isAfter(viagem.getDataRetorno())) {

	        throw new RegraNegocioException("Data da ocorrência não pode ser após o término da viagem");
	    }

	    OcorrenciaDuranteViagem ocorrencia;

	    if (dto instanceof DespesaViagemDTO despesaDTO) {
	        
	    	if (despesaDTO.getUnidade() == null) {
	    	    throw new RegraNegocioException("Unidade é obrigatória para despesa");
	    	}
	    	// Converter DTO para entidade DespesaViagem
	        ocorrencia = new DespesaViagem(
	                null, // id será gerado pelo DB
	                despesaDTO.getDescricao(),
	                despesaDTO.getData(),
	                despesaDTO.getValor(),
	                despesaDTO.getUnidade(),
	                despesaDTO.getQuantidade(),
	                despesaDTO.getDocumentoFiscal(),
	                despesaDTO.getKm()
	        );
	    } else if (dto instanceof MultaViagemDTO multaDTO) {
	        // Converter DTO para entidade MultaViagem
	        ocorrencia = new MultaViagem(
	                null, // id será gerado pelo DB
	                multaDTO.getDescricao(),
	                multaDTO.getData(),
	                multaDTO.getValor(),
	                multaDTO.getAutoInfracao(),
	                multaDTO.getHorario(),
	                multaDTO.getMunicipio(),
	                multaDTO.getDescricaoMulta()
	        );
	    } else {
	        throw new RegraNegocioException("Tipo de ocorrência inválido");
	    }

	    // Associar a ocorrência à viagem
	    viagem.getOcorrencias().add(ocorrencia);

	    // Salvar a ocorrência no banco
	    ocorrencia = ocorrenciasRepository.save(ocorrencia);

	    // Retornar DTO correspondente da entidade salva
	    return ocorrencia.toDTO(dto.getViagemId());
	}
	
	public OcorrenciaDuranteViagemDTO update(Long id, OcorrenciaDuranteViagemDTO dto) {

	    OcorrenciaDuranteViagem entity = ocorrenciasRepository.findById(id)
	            .orElseThrow(() -> new RecursoNaoEncontradoException("Ocorrência não encontrada"));

	    // Não permite trocar tipo
	    if (!entity.getClass().getSimpleName()
	            .equals(dto.getClass().getSimpleName().replace("DTO", ""))) {
	        throw new RegraNegocioException("Não é permitido alterar o tipo da ocorrência");
	    }

	    // Regra de negócio: data dentro da viagem
	    Viagem viagem = entity.getViagem();

	    if (viagem.getDataViagem() == null || viagem.getDataRetorno() == null) {
	        throw new RegraNegocioException("Viagem sem período válido");
	    }

	    if (dto.getData().isBefore(viagem.getDataViagem())
	            || dto.getData().isAfter(viagem.getDataRetorno())) {
	        throw new RegraNegocioException("Data fora do período da viagem");
	    }

	    // Campos comuns 
	    entity.setDescricao(dto.getDescricao());
	    entity.setData(dto.getData());
	    entity.setValor(dto.getValor());

	    // Campos específicos
	    if (entity instanceof DespesaViagem despesa && dto instanceof DespesaViagemDTO d) {
	        despesa.setQuantidade(d.getQuantidade());
	        despesa.setUnidade(d.getUnidade());
	        despesa.setKm(d.getKm());
	    }

	    if (entity instanceof MultaViagem multa && dto instanceof MultaViagemDTO m) {
	        multa.setAutoInfracao(m.getAutoInfracao());
	        multa.setMunicipio(m.getMunicipio());
	        multa.setHorario(m.getHorario());
	    }

	    entity = ocorrenciasRepository.save(entity);

	    Long idViagem = entity.getViagem() != null ? entity.getViagem().getId() : null;

	    return entity.toDTO(idViagem);
	}
	
	public void delete(Long id) {
	    if (!ocorrenciasRepository.existsById(id)) {
	        throw new RecursoNaoEncontradoException("Ocorrência não encontrada");
	    }
	    ocorrenciasRepository.deleteById(id);
	}


}
