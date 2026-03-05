package com.fabiana.mapa_viagem.controller;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fabiana.mapa_viagem.dto.AgendamentoDTO;
import com.fabiana.mapa_viagem.service.AgendamentoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/agendamentos")
public class AgendamentoController {
	
	@Autowired
	private AgendamentoService agendamentoService;
	
	@PostMapping
	public ResponseEntity<AgendamentoDTO> insert(@Valid @RequestBody AgendamentoDTO dto){
		AgendamentoDTO objDto = agendamentoService.insert(dto);
		 URI uri = ServletUriComponentsBuilder
				   .fromCurrentRequest()
				   .path("/{id}")
				   .buildAndExpand(objDto.getId())
		           .toUri();
		 return ResponseEntity.created(uri).body(objDto);
	}


}
