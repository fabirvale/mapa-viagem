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

import com.fabiana.mapa_viagem.dto.MotoristaDTO;
import com.fabiana.mapa_viagem.dto.VeiculoDTO;
import com.fabiana.mapa_viagem.service.MotoristaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/motoristas")
public class MotoristaController {
	
 @Autowired
  private MotoristaService motoristaService;
 
 @GetMapping
  public ResponseEntity<List<MotoristaDTO>> findAll(){
	 List<MotoristaDTO> listDto = motoristaService.findAll();
		return ResponseEntity.ok(listDto);
	 
  } 
 
 @GetMapping(value = "/{id}")
 public ResponseEntity<MotoristaDTO> findById(@PathVariable Long id){
	  MotoristaDTO dto = motoristaService.findById(id);
		return ResponseEntity.ok(dto); 
 } 
 
 @PostMapping
 public ResponseEntity<MotoristaDTO> insert(@Valid @RequestBody MotoristaDTO dto) {
     MotoristaDTO objDto = motoristaService.insert(dto);
     URI uri = ServletUriComponentsBuilder
             .fromCurrentRequest()
             .path("/{id}")
             .buildAndExpand(objDto.getId())
             .toUri();
     return ResponseEntity.created(uri).body(objDto);
 }
 
 @DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		motoristaService.delete(id);
		return ResponseEntity.noContent().build();
	}
	
	
@PutMapping(value = "/{id}")
	public ResponseEntity<Void> update(@PathVariable Long id,@Valid @RequestBody MotoristaDTO dto) {
     motoristaService.update(id, dto);
     return ResponseEntity.noContent().build();
 }


         
}
