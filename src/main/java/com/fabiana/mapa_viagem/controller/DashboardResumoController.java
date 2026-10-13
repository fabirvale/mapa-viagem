package com.fabiana.mapa_viagem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fabiana.mapa_viagem.dto.DashboardResumoDTO;
import com.fabiana.mapa_viagem.service.DashboardResumoService;

@RestController
@RequestMapping("/dashboard")
public class DashboardResumoController {
	
	 @Autowired
	   private DashboardResumoService dashboardResumoService;

	    @GetMapping("/resumo")
	    public ResponseEntity<DashboardResumoDTO> resumo() {
	        return ResponseEntity.ok(dashboardResumoService.buscarResumo());
	    }


}
