package com.fabiana.mapa_viagem.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fabiana.mapa_viagem.dto.TipoEspecialidadeDTO;
import com.fabiana.mapa_viagem.service.TipoEspecialidadeService;

@RestController
@RequestMapping(value = "/especialidades")
public class TipoEspecialidadeController {

	@Autowired
	private TipoEspecialidadeService tipoEspecialidadeService;
	
	@GetMapping
	public ResponseEntity<List<TipoEspecialidadeDTO>> findAll() {
		List<TipoEspecialidadeDTO> listDto = tipoEspecialidadeService.findAll();
		return ResponseEntity.ok(listDto);
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<TipoEspecialidadeDTO> findById(@PathVariable Long id) {
		TipoEspecialidadeDTO dto = tipoEspecialidadeService.findById(id);
		   return ResponseEntity.ok(dto);
	}
	
	
}
