package com.fabiana.mapa_viagem.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public class FecharViagemRequestDTO {
	
    @Valid @NotNull
    private ViagemDTO viagem;

    @Valid @NotNull
    private FecharViagemDTO pagamento;

	public FecharViagemRequestDTO() {
		
	}

	public ViagemDTO getViagem() {
		return viagem;
	}

	public void setViagem(ViagemDTO viagem) {
		this.viagem = viagem;
	}

	public FecharViagemDTO getPagamento() {
		return pagamento;
	}

	public void setPagamento(FecharViagemDTO pagamento) {
		this.pagamento = pagamento;
	}
    
    

}
