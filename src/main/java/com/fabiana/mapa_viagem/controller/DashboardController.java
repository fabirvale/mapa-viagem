package com.fabiana.mapa_viagem.controller;

import java.time.YearMonth;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fabiana.mapa_viagem.dto.PontoGraficoDTO;
import com.fabiana.mapa_viagem.dto.ViagensMesDTO;
import com.fabiana.mapa_viagem.service.DashboardService;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {
	
	 private final DashboardService dashboardService;

	    public DashboardController(DashboardService dashboardService) {
	        this.dashboardService = dashboardService;
	    }

	    @GetMapping("/diarias-por-motorista")
	    public List<PontoGraficoDTO> diariasPorMotorista(
	            @RequestParam @DateTimeFormat(pattern = "yyyy-MM") YearMonth mes) {
	        return dashboardService.diariasPorMotorista(mes);
	    }
	    
	    @GetMapping("/tipos-de-diaria")
	    public List<PontoGraficoDTO> tiposDeDiaria(
	            @RequestParam @DateTimeFormat(pattern = "yyyy-MM") YearMonth mes) {
	        return dashboardService.viagensPorTipoDiaria(mes);
	    }
	    
	    @GetMapping("/viagens-por-status")
	    public List<PontoGraficoDTO> viagensPorStatus(
	            @RequestParam @DateTimeFormat(pattern = "yyyy-MM") YearMonth mes) {
	        return dashboardService.viagensPorStatus(mes);
	    }
	    
	    @GetMapping("/viagens-por-mes")
	    public List<ViagensMesDTO> viagensPorMes(
	            @RequestParam @DateTimeFormat(pattern = "yyyy-MM") YearMonth mes) {
	        return dashboardService.viagensPorMes(mes);
	    }
	    
	    @GetMapping("/cancelamentos-por-mes")
	    public List<PontoGraficoDTO> cancelamentosPorMes(
	            @RequestParam @DateTimeFormat(pattern = "yyyy-MM") YearMonth mes) {
	        return dashboardService.cancelamentosPorMes(mes);
	    }
	    
	    @GetMapping("/cancelamentos-por-motivo")
	    public List<PontoGraficoDTO> cancelamentosPorMotivo(
	            @RequestParam @DateTimeFormat(pattern = "yyyy-MM") YearMonth mes) {
	        return dashboardService.cancelamentosPorMotivo(mes);
	    }

	    @GetMapping("/atendimentos-por-especialidade")
	    public List<PontoGraficoDTO> atendimentosPorEspecialidade(
	            @RequestParam @DateTimeFormat(pattern = "yyyy-MM") YearMonth mes) {
	        return dashboardService.atendimentosPorEspecialidade(mes);
	    }
	    
	    @GetMapping("/custo-ocorrencias-por-veiculo")
	    public List<PontoGraficoDTO> custoOcorrenciasPorVeiculo(
	            @RequestParam @DateTimeFormat(pattern = "yyyy-MM") YearMonth mes) {
	        return dashboardService.custoOcorrenciasPorVeiculo(mes);
	    }

	    @GetMapping("/multas-por-motorista")
	    public List<PontoGraficoDTO> multasPorMotorista(
	            @RequestParam @DateTimeFormat(pattern = "yyyy-MM") YearMonth mes) {
	        return dashboardService.multasPorMotorista(mes);
	    }

}
