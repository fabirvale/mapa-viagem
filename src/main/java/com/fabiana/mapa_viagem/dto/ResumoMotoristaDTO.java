package com.fabiana.mapa_viagem.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class ResumoMotoristaDTO {
	
	private String motoristaNome;
    private LocalDate inicio;
    private LocalDate fim;
    private List<LinhaResumoDTO> viagens;
    private long viagensPendentes;
    private BigDecimal totalAReceber;
    
	public ResumoMotoristaDTO() {
		
	}

	public ResumoMotoristaDTO(String motoristaNome, LocalDate inicio, LocalDate fim, List<LinhaResumoDTO> viagens,
			long viagensPendentes, BigDecimal totalAReceber) {
		this.motoristaNome = motoristaNome;
		this.inicio = inicio;
		this.fim = fim;
		this.viagens = viagens;
		this.viagensPendentes = viagensPendentes;
		this.totalAReceber = totalAReceber;
	}

	public String getMotoristaNome() {
		return motoristaNome;
	}

	public LocalDate getInicio() {
		return inicio;
	}

	public LocalDate getFim() {
		return fim;
	}

	public List<LinhaResumoDTO> getViagens() {
		return viagens;
	}

	public long getViagensPendentes() {
		return viagensPendentes;
	}

	public BigDecimal getTotalAReceber() {
		return totalAReceber;
	}
    
	
    

}
