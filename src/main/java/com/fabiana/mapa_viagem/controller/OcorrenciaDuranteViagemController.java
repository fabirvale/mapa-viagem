package com.fabiana.mapa_viagem.controller;

import java.net.URI;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fabiana.mapa_viagem.dto.DespesaViagemDTO;
import com.fabiana.mapa_viagem.dto.MultaViagemDTO;
import com.fabiana.mapa_viagem.dto.OcorrenciaDuranteViagemDTO;
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
	
	@GetMapping
	public ResponseEntity<List<OcorrenciaDuranteViagemDTO>> findAll() {
		List<OcorrenciaDuranteViagemDTO> listDto = ocorrenciasService.findAll();
		return ResponseEntity.ok(listDto);
	}
	
	@GetMapping(value = "/viagens/{viagemId}")
	public ResponseEntity<List<OcorrenciaDuranteViagemDTO>> findByViagemId(@PathVariable Long viagemId) {
		List<OcorrenciaDuranteViagemDTO> listDto = ocorrenciasService.findViagemId(viagemId);
		return ResponseEntity.ok(listDto);
	}
	
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<OcorrenciaDuranteViagemDTO> findById(@PathVariable Long id) {
		OcorrenciaDuranteViagemDTO listDto = ocorrenciasService.findById(id);
		return ResponseEntity.ok(listDto);
	}
	
	
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
	
	@PutMapping("/{id}")
	public ResponseEntity<OcorrenciaDuranteViagemDTO> update(@PathVariable Long id, @RequestBody Map<String, Object> body) {
	    OcorrenciaDuranteViagemDTO dto = converterParaDTO(body);
	    validar(dto);
	    OcorrenciaDuranteViagemDTO updated = ocorrenciasService.update(id, dto);
	    return ResponseEntity.ok(updated);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
	    ocorrenciasService.delete(id);
	    return ResponseEntity.noContent().build();
	}

    private OcorrenciaDuranteViagemDTO converterParaDTO(Map<String, Object> body) {
    	
    	 boolean isDespesa = body.get("unidade") != null;
    	 boolean isMulta = body.get("autoInfracao") != null;

         if (isDespesa && isMulta) {
    	        throw new RegraNegocioException("JSON inválido: múltiplos tipos");
    	 }
         if (isDespesa) {

             Object unidadeObj = body.get("unidade");

             if (unidadeObj != null) {
                 String unidadeNormalizada = unidadeObj.toString()
                         .trim()
                         .toUpperCase()
                         .replace("/", "_"); // trata N/A → NAO_APLICAVEL depois

                 if (unidadeNormalizada.equals("N_A")) {
                     unidadeNormalizada = "NAO_APLICAVEL";
                 }

                 body.put("unidade", unidadeNormalizada);
             }

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

