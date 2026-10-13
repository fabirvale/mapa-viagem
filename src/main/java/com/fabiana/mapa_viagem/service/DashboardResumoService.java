package com.fabiana.mapa_viagem.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fabiana.mapa_viagem.dto.DashboardResumoDTO;
import com.fabiana.mapa_viagem.enums.StatusViagem;
import com.fabiana.mapa_viagem.repository.AgendamentoRepository;
import com.fabiana.mapa_viagem.repository.ViagemRepository;

@Service
public class DashboardResumoService {
	
	@Autowired
	private ViagemRepository viagemRepository;
	
	@Autowired
	private AgendamentoRepository agendamentoRepository;
	
	 //Cards - resumo do número de viagens e pacientes
	 public DashboardResumoDTO buscarResumo() {

		    LocalDate hoje = LocalDate.now();
		    LocalDate seteDiasDepois = hoje.plusDays(7);
		    LocalDate inicioMes = hoje.withDayOfMonth(1);
		    LocalDate fimMes = hoje.withDayOfMonth(hoje.lengthOfMonth());

		    long viagensAgendadas = viagemRepository.contarAgendadasProximosDias(
		        StatusViagem.AGENDADA, hoje, seteDiasDepois);

		    long viagensHoje = viagemRepository.contarViagensHoje(hoje);

		    long pacientesAgendadosHoje = agendamentoRepository.contarPacientesAgendadosHoje(hoje);

		    long viagensFinalizadasMes = viagemRepository.contarFinalizadasNoMes(
		        StatusViagem.FINALIZADA, inicioMes, fimMes);

		    return new DashboardResumoDTO(viagensAgendadas, viagensHoje, pacientesAgendadosHoje, viagensFinalizadasMes);
		}

}
