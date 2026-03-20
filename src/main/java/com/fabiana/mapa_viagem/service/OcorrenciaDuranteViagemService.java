package com.fabiana.mapa_viagem.service;

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
	
	public OcorrenciaDuranteViagemDTO insert(OcorrenciaDuranteViagemDTO dto) {
	    // Buscar a viagem
	    Viagem viagem = viagemRepository.findById(dto.getViagemId())
	            .orElseThrow(() -> new RecursoNaoEncontradoException("Viagem não encontrada"));
	    
	    if (dto.getData().isBefore(viagem.getDataViagem()) ||
	    	    dto.getData().isAfter(viagem.getDataRetorno())) {

	    	    throw new RegraNegocioException("Data da ocorrência deve estar dentro do período da viagem");
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


}
