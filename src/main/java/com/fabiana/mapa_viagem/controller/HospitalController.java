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

import com.fabiana.mapa_viagem.dto.HospitalDTO;
import com.fabiana.mapa_viagem.service.HospitalService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/hospitais")
public class HospitalController {

	@Autowired
	private HospitalService hospitalService;
	
	@GetMapping
	public ResponseEntity<List<HospitalDTO>> findAll() {
		List<HospitalDTO> listDto = hospitalService.findAll();
		return ResponseEntity.ok(listDto);
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<HospitalDTO> findById(@PathVariable Long id) {
		HospitalDTO dto = hospitalService.findById(id);
		   return ResponseEntity.ok(dto);
	}
	
	@PostMapping
	public ResponseEntity<HospitalDTO> insert(@Valid @RequestBody HospitalDTO dto) {
		HospitalDTO objDto = hospitalService.insert(dto);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(objDto.getId())
                .toUri();

        return ResponseEntity.created(uri).body(objDto);
    }
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		hospitalService.delete(id);
		return ResponseEntity.noContent().build();
	}
	
	
	@PutMapping(value = "/{id}")
	public ResponseEntity<Void> update(@PathVariable Long id,@Valid @RequestBody HospitalDTO dto) {
        hospitalService.update(id, dto);
        return ResponseEntity.noContent().build();
    }


}
