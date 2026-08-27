package com.fabiana.mapa_viagem.exception;

public class AgendamentosVinculadosException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	private final int quantidadeAgendamentos;

    public AgendamentosVinculadosException(int quantidadeAgendamentos) {
        super("Existem " + quantidadeAgendamentos + " agendamento(s) vinculado(s) a esta viagem.");
        this.quantidadeAgendamentos = quantidadeAgendamentos;
    }

    public int getQuantidadeAgendamentos() {
        return quantidadeAgendamentos;
    }

}
