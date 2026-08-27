package com.fabiana.mapa_viagem.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fabiana.mapa_viagem.dto.MotivoCancelamentoDTO;
import com.fabiana.mapa_viagem.service.MotivoCancelamentoService;

@RestController
@RequestMapping(value = "/motivoscancelamento")
public class MotivoCancelamentoController {

	@Autowired
	private MotivoCancelamentoService motivoCancelamentoService;
	
	@GetMapping
	public ResponseEntity<List<MotivoCancelamentoDTO>> findAll() {
		List<MotivoCancelamentoDTO> listDto = motivoCancelamentoService.findAll();
		return ResponseEntity.ok(listDto);
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<MotivoCancelamentoDTO> findById(@PathVariable Long id) {
		MotivoCancelamentoDTO dto = motivoCancelamentoService.findById(id);
		   return ResponseEntity.ok(dto);
	}
	
	
}
