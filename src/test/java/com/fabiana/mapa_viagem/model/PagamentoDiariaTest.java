package com.fabiana.mapa_viagem.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import com.fabiana.mapa_viagem.enums.TipoDiaria;

public class PagamentoDiariaTest {
	@Test
	void testParcialSemPernoite() {
	    // Simula viagem curta: 6 horas, KM < 300
	    LocalDateTime saida = LocalDateTime.of(2026, 3, 26, 8, 0);
	    LocalDateTime retorno = LocalDateTime.of(2026, 3, 26, 14, 0); // 6h de viagem

	    PagamentoDiaria pagamento = new PagamentoDiaria();
	    pagamento.setDataHoraSaida(saida);
	    pagamento.setDataHoraRetorno(retorno);

	    pagamento.definirTipoDiaria(100); // km percorridos

	    // Valida tipo
	    assertEquals(TipoDiaria.PARCIAL_SEM_PERNOITE, pagamento.getTipoDiaria());

	    // Valida valor do enum
	    assertEquals(new BigDecimal("56.00"), pagamento.getTipoDiaria().getValor());
	}
	
	@Test
	void testCompletaSemPernoite() {
	    // Simula viagem longa: 9 horas, KM < 300
	    LocalDateTime saida = LocalDateTime.of(2026, 3, 26, 8, 0);
	    LocalDateTime retorno = LocalDateTime.of(2026, 3, 26, 17, 0); // 9h de viagem

	    PagamentoDiaria pagamento = new PagamentoDiaria();
	    pagamento.setDataHoraSaida(saida);
	    pagamento.setDataHoraRetorno(retorno);

	    pagamento.definirTipoDiaria(200); // km percorridos

	    assertEquals(TipoDiaria.COMPLETA_SEM_PERNOITE, pagamento.getTipoDiaria());
	    assertEquals(new BigDecimal("105.00"), pagamento.getTipoDiaria().getValor());
	}
	
	@Test
	void testCompletaComPernoitePorKM() {
	    // Simula viagem longa, KM >= 300 mesmo sem mudar de dia
	    LocalDateTime saida = LocalDateTime.of(2026, 3, 26, 8, 0);
	    LocalDateTime retorno = LocalDateTime.of(2026, 3, 26, 12, 0); // 4h de viagem

	    PagamentoDiaria pagamento = new PagamentoDiaria();
	    pagamento.setDataHoraSaida(saida);
	    pagamento.setDataHoraRetorno(retorno);

	    pagamento.definirTipoDiaria(301); // KM >= 300

	    assertEquals(TipoDiaria.COMPLETA_COM_PERNOITE, pagamento.getTipoDiaria());
	    assertEquals(new BigDecimal("420.00"), pagamento.getTipoDiaria().getValor());
	}

	@Test
	void testCompletaComPernoitePorViradaDia() {
	    // Simula virada de dia
	    LocalDateTime saida = LocalDateTime.of(2026, 3, 26, 22, 0);
	    LocalDateTime retorno = LocalDateTime.of(2026, 3, 27, 6, 0); // mudança de dia

	    PagamentoDiaria pagamento = new PagamentoDiaria();
	    pagamento.setDataHoraSaida(saida);
	    pagamento.setDataHoraRetorno(retorno);

	    pagamento.definirTipoDiaria(200); // KM < 300, mas virada de dia

	    assertEquals(TipoDiaria.COMPLETA_COM_PERNOITE, pagamento.getTipoDiaria());
	    assertEquals(new BigDecimal("420.00"), pagamento.getTipoDiaria().getValor());
	}

}
