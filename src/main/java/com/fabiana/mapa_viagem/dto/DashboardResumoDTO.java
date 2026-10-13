package com.fabiana.mapa_viagem.dto;

public class DashboardResumoDTO {
	
	private Long viagensAgendadas;
    private Long viagensHoje;
    private Long pacientesAgendadosHoje;
    private Long viagensFinalizadasMes;
    
	public DashboardResumoDTO(Long viagensAgendadas, Long viagensHoje, Long pacientesAgendadosHoje,
			Long viagensFinalizadasMes) {
	    this.viagensAgendadas = viagensAgendadas;
		this.viagensHoje = viagensHoje;
		this.pacientesAgendadosHoje = pacientesAgendadosHoje;
		this.viagensFinalizadasMes = viagensFinalizadasMes;
	}
	public Long getViagensAgendadas() {
		return viagensAgendadas;
	}
	public void setViagensAgendadas(Long viagensAgendadas) {
		this.viagensAgendadas = viagensAgendadas;
	}
	public Long getViagensHoje() {
		return viagensHoje;
	}
	public void setViagensHoje(Long viagensHoje) {
		this.viagensHoje = viagensHoje;
	}
	public Long getPacientesAgendadosHoje() {
		return pacientesAgendadosHoje;
	}
	public void setPacientesAgendadosHoje(Long pacientesAgendadosHoje) {
		this.pacientesAgendadosHoje = pacientesAgendadosHoje;
	}
	public Long getViagensFinalizadasMes() {
		return viagensFinalizadasMes;
	}
	public void setViagensFinalizadasMes(Long viagensFinalizadasMes) {
		this.viagensFinalizadasMes = viagensFinalizadasMes;
	}



}
