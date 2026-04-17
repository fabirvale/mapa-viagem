package com.fabiana.mapa_viagem.controller;

import java.net.URI;
import java.util.List;

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

import com.fabiana.mapa_viagem.dto.AgendamentoDTO;
import com.fabiana.mapa_viagem.service.AgendamentoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/agendamentos")
public class AgendamentoController {
	
	@Autowired
	private AgendamentoService agendamentoService;
	
	@GetMapping
	public ResponseEntity<List<AgendamentoDTO>> findAll() {
		List<AgendamentoDTO> listDto = agendamentoService.findAll();
		return ResponseEntity.ok(listDto);
	}
	
	@GetMapping("/viagem/{viagemId}")
	public ResponseEntity<List<AgendamentoDTO>> listarPorViagem(@PathVariable Long viagemId) {
		List<AgendamentoDTO> listDto = agendamentoService.listarPorViagem(viagemId);
		return ResponseEntity.ok(listDto);
	}
	
		
	@GetMapping(value = "/{id}")
	public ResponseEntity<AgendamentoDTO> findById(@PathVariable Long id) {
		AgendamentoDTO listDto = agendamentoService.findById(id);
		return ResponseEntity.ok(listDto);
	}
	
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
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		agendamentoService.delete(id);
		return ResponseEntity.noContent().build();
	}
	
	
	@PutMapping(value = "/{id}")
	public ResponseEntity<Void> update(@PathVariable Long id,@Valid @RequestBody AgendamentoDTO dto) {
        agendamentoService.update(id, dto);
        return ResponseEntity.noContent().build();
    }


}
