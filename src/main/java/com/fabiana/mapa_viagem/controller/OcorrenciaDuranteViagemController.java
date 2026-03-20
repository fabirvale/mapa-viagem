package com.fabiana.mapa_viagem.controller;

import java.net.URI;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fabiana.mapa_viagem.dto.DespesaViagemDTO;
import com.fabiana.mapa_viagem.dto.MultaViagemDTO;
import com.fabiana.mapa_viagem.dto.OcorrenciaDuranteViagemDTO;
import com.fabiana.mapa_viagem.enums.TipoUnidadeDespesa;
import com.fabiana.mapa_viagem.exception.RegraNegocioException;
import com.fabiana.mapa_viagem.service.OcorrenciaDuranteViagemService;

import tools.jackson.databind.ObjectMapper;

@RestController
@RequestMapping(value = "/ocorrencias")
public class OcorrenciaDuranteViagemController {
	
	@Autowired
	OcorrenciaDuranteViagemService ocorrenciasService;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@PostMapping
	public ResponseEntity<OcorrenciaDuranteViagemDTO> insert(@RequestBody Map<String, Object> body) {

        OcorrenciaDuranteViagemDTO dto = converterParaDTO(body);
        validar(dto);      
        OcorrenciaDuranteViagemDTO objDto = ocorrenciasService.insert(dto);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(objDto.getId())
                .toUri();

        return ResponseEntity.created(uri).body(objDto);
    }

    private OcorrenciaDuranteViagemDTO converterParaDTO(Map<String, Object> body) {

        //Caso seja DESPESA
        if (body.get("unidade") != null) {

            String unidadeStr = body.get("unidade").toString();

            try {
                // valida se o enum é válido
                TipoUnidadeDespesa.valueOf(unidadeStr);
            } catch (IllegalArgumentException e) {
                throw new RegraNegocioException("Unidade inválida: " + unidadeStr);
            }
            
            //Converter dados JSON em Objetos Java ou vice e versa
            return objectMapper.convertValue(body, DespesaViagemDTO.class);
        }

        //Caso seja MULTA
        if (body.get("autoInfracao") != null) {
            return objectMapper.convertValue(body, MultaViagemDTO.class);
        }

        //Caso não identifique
        throw new RegraNegocioException("Tipo de ocorrência inválido");
    }
    
    private void validar(OcorrenciaDuranteViagemDTO dto) {
        validarBase(dto);

        if (dto instanceof DespesaViagemDTO d) {
            validarDespesa(d);
        } else if (dto instanceof MultaViagemDTO m) {
            validarMulta(m);
        } else {
            throw new RegraNegocioException("Tipo inválido");
        }
    }
    
    private void validarBase(OcorrenciaDuranteViagemDTO dto) {

        if (dto.getDescricao() == null || dto.getDescricao().isBlank()) {
            throw new RegraNegocioException("Descrição é obrigatória");
        }

        if (dto.getDescricao().length() > 255) {
            throw new RegraNegocioException("Descrição deve ter no máximo 255 caracteres");
        }

        if (dto.getData() == null) {
            throw new RegraNegocioException("Data é obrigatória");
        }

        if (dto.getValor() == null) {
            throw new RegraNegocioException("Valor é obrigatório");
        }
    }
    
    private void validarDespesa(DespesaViagemDTO dto) {

        if (dto.getUnidade() == null) {
            throw new RegraNegocioException("Unidade obrigatória");
        }

        if (dto.getQuantidade() == null || dto.getQuantidade() <= 0) {
            throw new RegraNegocioException("Quantidade inválida");
        }

        if (dto.getKm() < 0) {
            throw new RegraNegocioException("Km inválido");
        }
    }
    
    private void validarMulta(MultaViagemDTO dto) {

        if (dto.getAutoInfracao() == null || dto.getAutoInfracao().isBlank()) {
            throw new RegraNegocioException("Auto de infração obrigatório");
        }

        if (dto.getMunicipio() == null || dto.getMunicipio().isBlank()) {
            throw new RegraNegocioException("Município obrigatório");
        }

        if (dto.getHorario() == null) {
            throw new RegraNegocioException("Horário obrigatório");
        }
    }

}

