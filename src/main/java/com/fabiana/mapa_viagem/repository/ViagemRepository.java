package com.fabiana.mapa_viagem.repository;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fabiana.mapa_viagem.enums.StatusViagem;
import com.fabiana.mapa_viagem.model.Viagem;

public interface ViagemRepository extends JpaRepository<Viagem, Long>{
	
	 boolean existsByCidadeOrigemAndCidadeDestinoAndDataViagemAndHoraPrevista(
	            String cidadeOrigem,
	            String cidadeDestino,
	            LocalDate dataViagem,
	            LocalTime horaPrevista,
	            StatusViagem status);


}
