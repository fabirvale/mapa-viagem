package com.fabiana.mapa_viagem.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fabiana.mapa_viagem.dto.PontoGraficoDTO;
import com.fabiana.mapa_viagem.dto.ViagensMesDTO;
import com.fabiana.mapa_viagem.enums.StatusAgendamento;
import com.fabiana.mapa_viagem.enums.StatusViagem;
import com.fabiana.mapa_viagem.model.MultaViagem;
import com.fabiana.mapa_viagem.model.OcorrenciaDuranteViagem;
import com.fabiana.mapa_viagem.model.Viagem;
import com.fabiana.mapa_viagem.repository.AgendamentoRepository;
import com.fabiana.mapa_viagem.repository.ViagemRepository;

@Service
public class DashboardService {
	
	   private final ViagemRepository viagemRepository;
	   private final AgendamentoRepository agendamentoRepository;

	   	    
	    public DashboardService(ViagemRepository viagemRepository, AgendamentoRepository agendamentoRepository) {
			this.viagemRepository = viagemRepository;
			this.agendamentoRepository = agendamentoRepository;
        }

	    @Transactional(readOnly = true)
	    public List<PontoGraficoDTO> diariasPorMotorista(YearMonth mes) {
	        return viagemRepository.diariasPorMotorista(
	                StatusViagem.FINALIZADA, mes.atDay(1), mes.atEndOfMonth());
	    }
	    
	    @Transactional(readOnly = true)
	    public List<PontoGraficoDTO> viagensPorTipoDiaria(YearMonth mes) {
	        return viagemRepository.viagensPorTipoDiaria(
	                StatusViagem.FINALIZADA, mes.atDay(1), mes.atEndOfMonth());
	    }
	    
	    @Transactional(readOnly = true)
	    public List<PontoGraficoDTO> viagensPorStatus(YearMonth mes) {
	        return viagemRepository.viagensPorStatus(mes.atDay(1), mes.atEndOfMonth());
	    }
	    
	
		@Transactional(readOnly = true)
		public List<ViagensMesDTO> viagensPorMes(YearMonth mesFinal) {

		    YearMonth mesInicial = mesFinal.minusMonths(11);

		    List<Object[]> linhas = viagemRepository.viagensPorMesEStatus(
		            List.of(StatusViagem.FINALIZADA, StatusViagem.CANCELADA),
		            mesInicial.atDay(1), mesFinal.atEndOfMonth());

		    // mapa mês -> [finalizadas, canceladas], já com os 12 meses zerados
		    Map<YearMonth, long[]> mapa = new LinkedHashMap<>();
		    for (int i = 0; i < 12; i++) {
		        mapa.put(mesInicial.plusMonths(i), new long[] {0, 0});
		    }

		    for (Object[] l : linhas) {
		        YearMonth ym = YearMonth.of(((Number) l[0]).intValue(), ((Number) l[1]).intValue());
		        StatusViagem status = (StatusViagem) l[2];
		        long qtd = ((Number) l[3]).longValue();

		        long[] contagem = mapa.get(ym);
		        if (contagem == null) continue;
		        if (status == StatusViagem.FINALIZADA) contagem[0] = qtd;
		        else contagem[1] = qtd;
		    }

		    List<ViagensMesDTO> resultado = new ArrayList<>();
		    mapa.forEach((ym, c) -> resultado.add(new ViagensMesDTO(ym.toString(), c[0], c[1])));
		    return resultado;
		}
		
		@Transactional(readOnly = true)
		public List<PontoGraficoDTO> cancelamentosPorMes(YearMonth mesFinal) {

		    YearMonth mesInicial = mesFinal.minusMonths(11);

		    List<Object[]> linhas = agendamentoRepository.cancelamentosPorMes(
		            StatusAgendamento.CANCELADO, mesInicial.atDay(1), mesFinal.atEndOfMonth());

		    // 12 meses, todos zerados
		    Map<YearMonth, Long> mapa = new LinkedHashMap<>();
		    for (int i = 0; i < 12; i++) {
		        mapa.put(mesInicial.plusMonths(i), 0L);
		    }

		    for (Object[] l : linhas) {
		        YearMonth ym = YearMonth.of(((Number) l[0]).intValue(), ((Number) l[1]).intValue());
		        if (mapa.containsKey(ym)) {
		            mapa.put(ym, ((Number) l[2]).longValue());
		        }
		    }

		    List<PontoGraficoDTO> resultado = new ArrayList<>();
		    mapa.forEach((ym, qtd) -> resultado.add(new PontoGraficoDTO(ym.toString(), qtd)));
		    return resultado;
		}
		
		@Transactional(readOnly = true)
		public List<PontoGraficoDTO> cancelamentosPorMotivo(YearMonth mes) {
		    return agendamentoRepository.cancelamentosPorMotivo(
		            StatusAgendamento.CANCELADO, mes.atDay(1), mes.atEndOfMonth());
		}

		@Transactional(readOnly = true)
		public List<PontoGraficoDTO> atendimentosPorEspecialidade(YearMonth mes) {
		    return agendamentoRepository.atendimentosPorEspecialidade(
		            StatusAgendamento.CANCELADO, mes.atDay(1), mes.atEndOfMonth());
		}
		
		@Transactional(readOnly = true)
		public List<PontoGraficoDTO> custoOcorrenciasPorVeiculo(YearMonth mes) {

		    LocalDate inicio = mes.atDay(1);
		    LocalDate fim = mes.atEndOfMonth();
		    Map<String, BigDecimal> totais = new LinkedHashMap<>();

		    for (Viagem v : viagemRepository.viagensComOcorrenciasNoPeriodo(inicio, fim)) {
		        String rotulo = v.getVeiculo().getModelo() + " - " + v.getVeiculo().getPlaca();

		        for (OcorrenciaDuranteViagem o : v.getOcorrencias()) {
		            if (o.getData() == null || o.getData().isBefore(inicio) || o.getData().isAfter(fim)) continue;
		            totais.merge(rotulo, o.calcularValor(), BigDecimal::add);
		        }
		    }

		    return totais.entrySet().stream()
		            .filter(e -> e.getValue().compareTo(BigDecimal.ZERO) > 0)
		            .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
		            .map(e -> new PontoGraficoDTO(e.getKey(), e.getValue()))
		            .toList();
		}

		@Transactional(readOnly = true)
		public List<PontoGraficoDTO> multasPorMotorista(YearMonth mes) {

		    LocalDate inicio = mes.atDay(1);
		    LocalDate fim = mes.atEndOfMonth();
		    Map<String, BigDecimal> totais = new LinkedHashMap<>();

		    for (Viagem v : viagemRepository.viagensComOcorrenciasNoPeriodo(inicio, fim)) {
		        String motorista = v.getMotorista().getNome();

		        for (OcorrenciaDuranteViagem o : v.getOcorrencias()) {
		            if (!(o instanceof MultaViagem)) continue;
		            if (o.getData() == null || o.getData().isBefore(inicio) || o.getData().isAfter(fim)) continue;
		            totais.merge(motorista, o.calcularValor(), BigDecimal::add);
		        }
		    }

		    return totais.entrySet().stream()
		            .filter(e -> e.getValue().compareTo(BigDecimal.ZERO) > 0)
		            .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
		            .map(e -> new PontoGraficoDTO(e.getKey(), e.getValue()))
		            .toList();
		}
		
}





