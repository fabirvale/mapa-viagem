package com.fabiana.mapa_viagem.dto;

public class ViagensMesDTO {
	
	private String mes;          
    private long finalizadas;
    private long canceladas;

    public ViagensMesDTO() {
    }

    public ViagensMesDTO(String mes, long finalizadas, long canceladas) {
        this.mes = mes;
        this.finalizadas = finalizadas;
        this.canceladas = canceladas;
    }

    public String getMes() {
    	return mes;
    }
    
    public long getFinalizadas() {
    	return finalizadas;
    }
    
    public long getCanceladas() {
    	return canceladas;
    }

}
