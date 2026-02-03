package com.fabiana.mapa_viagem.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fabiana.mapa_viagem.model.Agendamento;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {

}
