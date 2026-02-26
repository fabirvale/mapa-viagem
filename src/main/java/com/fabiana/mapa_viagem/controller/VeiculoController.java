package com.fabiana.mapa_viagem.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fabiana.mapa_viagem.dto.VeiculoDTO;
import com.fabiana.mapa_viagem.service.VeiculoService;

@RestController
@RequestMapping(value = "/veiculos")
public class VeiculoController {

	@Autowired
	private VeiculoService veiculoService;
	
	@GetMapping
	public ResponseEntity<List<VeiculoDTO>> findAll() {
		List<VeiculoDTO> listDto = veiculoService.findAll();
		return ResponseEntity.ok(listDto);
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<VeiculoDTO> findById(@PathVariable Long id) {
		  VeiculoDTO dto = veiculoService.findById(id);
		   return ResponseEntity.ok(dto);
	}

}
