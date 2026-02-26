package com.fabiana.mapa_viagem.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fabiana.mapa_viagem.model.Paciente;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {

}
