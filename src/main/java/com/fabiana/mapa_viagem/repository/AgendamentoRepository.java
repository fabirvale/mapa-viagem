package com.fabiana.mapa_viagem.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fabiana.mapa_viagem.model.Agendamento;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {
	
	// Busca um agendamento por paciente e viagem
    Optional<Agendamento> findByPacienteIdAndViagemId(Long pacienteId, Long viagemId);
    
    //Busca agendamento por viagem
    List<Agendamento> findByViagemId(Long viagemId);
  
    //Verifica se existe ao menos um agendamento, retorna boolean
    boolean existsByViagemId(Long viagemId);
}
