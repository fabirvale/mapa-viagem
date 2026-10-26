package com.fabiana.mapa_viagem.controller;

import java.net.URI;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fabiana.mapa_viagem.dto.FecharViagemRequestDTO;
import com.fabiana.mapa_viagem.dto.FecharViagemResponseDTO;
import com.fabiana.mapa_viagem.dto.ResumoMotoristaDTO;
import com.fabiana.mapa_viagem.dto.ViagemDTO;
import com.fabiana.mapa_viagem.dto.ViagensMesDTO;
import com.fabiana.mapa_viagem.enums.StatusViagem;
import com.fabiana.mapa_viagem.service.DashboardService;
import com.fabiana.mapa_viagem.service.ViagemService;

import jakarta.validation.Valid;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/viagens")
public class ViagemController {

	@Autowired
	private ViagemService viagemService;
	
	@Autowired
	private DashboardService dashboardService;

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
	
		
	@PutMapping(value = "/{id}/fechar")
	public ResponseEntity<FecharViagemResponseDTO> fecharViagem(@PathVariable Long id, @RequestBody FecharViagemRequestDTO request) {
	    FecharViagemResponseDTO resultado = viagemService.fecharViagem(id, request.getViagem(), request.getPagamento());
	    return ResponseEntity.ok(resultado);
	}
	
			
		//Iniciar viagem
		@PatchMapping("/{id}/iniciar")
		public ResponseEntity<Void> iniciarViagem(@PathVariable Long id) {
		    viagemService.iniciarViagem(id);
		    return ResponseEntity.noContent().build();
		}
		
		//Cancelar viagem
		@PatchMapping("/{id}/cancelar")
		public ResponseEntity<Void> cancelarViagem(@PathVariable Long id, @RequestBody ViagemDTO dto) {
			viagemService.cancelarViagem(id, dto);
			return ResponseEntity.noContent().build();
        }
		
     //Buscar o status
		@GetMapping("/status")
		public ResponseEntity<StatusViagem[]> listarStatus() {
			 return ResponseEntity.ok(StatusViagem.values());
		}
		
		//Filtro
		@GetMapping("/filtros")
		public ResponseEntity<List<ViagemDTO>> listar(
	            @RequestParam(required = false) String busca,
	            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial,
	            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal,
	            @RequestParam(required = false) StatusViagem status,
	            @RequestParam(required = false) Long motoristaId) {

	        List<ViagemDTO> viagens = viagemService.buscarComFiltros(busca, dataInicial, dataFinal, status,motoristaId);
	        return ResponseEntity.ok(viagens);
	    }
		

		@PutMapping("/{id}/reabrir")
		public ResponseEntity<Void> reabrir(@PathVariable Long id) {
		    viagemService.reabrirViagem(id);
		    return ResponseEntity.noContent().build();
		}
		

		//Report com total da diária do motorista
		@GetMapping("/motoristas/{id}/resumo")
		public ResumoMotoristaDTO resumo(@PathVariable Long id,
		        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
		        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
		    return viagemService.resumoMotorista(id, inicio, fim);
		}
	
		
		//Gráfico
		@GetMapping("/viagens-por-mes")
		public List<ViagensMesDTO> viagensPorMes(
		        @RequestParam @DateTimeFormat(pattern = "yyyy-MM") YearMonth mes) {
		    return dashboardService.viagensPorMes(mes);
		}

}
