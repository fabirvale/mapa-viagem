package com.fabiana.mapa_viagem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fabiana.mapa_viagem.model.OcorrenciaDuranteViagem;

public interface OcorrenciaDuranteViagemRepository extends JpaRepository<OcorrenciaDuranteViagem, Long>{
	
	// Busca um agendamento por paciente e viagem
	 List<OcorrenciaDuranteViagem> findByViagemId(Long viagemId);

}
