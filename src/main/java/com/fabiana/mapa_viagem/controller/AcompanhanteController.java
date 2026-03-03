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

import com.fabiana.mapa_viagem.dto.AcompanhanteDTO;
import com.fabiana.mapa_viagem.service.AcompanhanteService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/acompanhantes")
public class AcompanhanteController {
	
 @Autowired
  private AcompanhanteService acompanhanteService;
 
 @GetMapping
  public ResponseEntity<List<AcompanhanteDTO>> findAll(){
	 List<AcompanhanteDTO> listDto = acompanhanteService.findAll();
		return ResponseEntity.ok(listDto);
	 
  } 
 
 @GetMapping(value = "/{id}")
 public ResponseEntity<AcompanhanteDTO> findById(@PathVariable Long id){
	  AcompanhanteDTO dto = acompanhanteService.findById(id);
		return ResponseEntity.ok(dto); 
 } 
 
 @PostMapping
 public ResponseEntity<AcompanhanteDTO> insert(@Valid @RequestBody AcompanhanteDTO dto) {
     AcompanhanteDTO objDto = acompanhanteService.insert(dto);
     URI uri = ServletUriComponentsBuilder
             .fromCurrentRequest()
             .path("/{id}")
             .buildAndExpand(objDto.getId())
             .toUri();
     return ResponseEntity.created(uri).body(objDto);
 }
 
 @DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		acompanhanteService.delete(id);
		return ResponseEntity.noContent().build();
	}
	
	
@PutMapping(value = "/{id}")
	public ResponseEntity<Void> update(@PathVariable Long id,@Valid @RequestBody AcompanhanteDTO dto) {
     acompanhanteService.update(id, dto);
     return ResponseEntity.noContent().build();
 }


         
}
