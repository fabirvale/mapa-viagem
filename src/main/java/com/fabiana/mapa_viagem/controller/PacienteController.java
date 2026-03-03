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

import com.fabiana.mapa_viagem.dto.PacienteDTO;
import com.fabiana.mapa_viagem.service.PacienteService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/pacientes")
public class PacienteController {
	
 @Autowired
  private PacienteService pacienteService;
 
 @GetMapping
  public ResponseEntity<List<PacienteDTO>> findAll(){
	 List<PacienteDTO> listDto = pacienteService.findAll();
		return ResponseEntity.ok(listDto);
	 
  } 
 
 @GetMapping(value = "/{id}")
 public ResponseEntity<PacienteDTO> findById(@PathVariable Long id){
	  PacienteDTO dto = pacienteService.findById(id);
		return ResponseEntity.ok(dto); 
 } 
 
 @PostMapping
 public ResponseEntity<PacienteDTO> insert(@Valid @RequestBody PacienteDTO dto) {
     PacienteDTO objDto = pacienteService.insert(dto);
     URI uri = ServletUriComponentsBuilder
             .fromCurrentRequest()
             .path("/{id}")
             .buildAndExpand(objDto.getId())
             .toUri();
     return ResponseEntity.created(uri).body(objDto);
 }
 
 @DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		pacienteService.delete(id);
		return ResponseEntity.noContent().build();
	}
	
	
@PutMapping(value = "/{id}")
	public ResponseEntity<Void> update(@PathVariable Long id,@Valid @RequestBody PacienteDTO dto) {
     pacienteService.update(id, dto);
     return ResponseEntity.noContent().build();
 }


         
}
