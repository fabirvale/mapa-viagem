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

import com.fabiana.mapa_viagem.dto.ViagemDTO;
import com.fabiana.mapa_viagem.service.ViagemService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/viagens")
public class ViagemController {

	@Autowired
	private ViagemService viagemService;

	@GetMapping
	public ResponseEntity<List<ViagemDTO>> findAll() {
		List<ViagemDTO> listDto = viagemService.findAll();
		return ResponseEntity.ok(listDto);
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<ViagemDTO> findById(@PathVariable Long id) {
		ViagemDTO dto = viagemService.findById(id);
		return ResponseEntity.ok(dto);
	}
		
	
	@PostMapping
	public ResponseEntity<ViagemDTO> insert(@Valid @RequestBody ViagemDTO dto) {
        ViagemDTO objDto = viagemService.insert(dto);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(objDto.getId())
                .toUri();

        return ResponseEntity.created(uri).body(objDto);
    }

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		viagemService.delete(id);
		return ResponseEntity.noContent().build();
	}
	
	
	@PutMapping(value = "/{id}")
	public ResponseEntity<Void> update(@PathVariable Long id,@Valid @RequestBody ViagemDTO dto) {
        viagemService.update(id, dto);
        return ResponseEntity.noContent().build();
    }

}
