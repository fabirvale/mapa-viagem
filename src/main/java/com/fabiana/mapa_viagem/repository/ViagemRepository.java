package com.fabiana.mapa_viagem.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.fabiana.mapa_viagem.enums.StatusViagem;
import com.fabiana.mapa_viagem.model.Viagem;

public interface ViagemRepository extends JpaRepository<Viagem, Long>{
	
	 boolean existsByCidadeOrigemAndCidadeDestinoAndDataViagemAndHoraPrevista(
	            String cidadeOrigem,
	            String cidadeDestino,
	            LocalDate dataViagem,
	            LocalTime horaPrevista,
	            StatusViagem status);
	 
	 
	 @Query("SELECT v FROM Viagem v WHERE " +
	           "(:busca IS NULL OR LOWER(v.cidadeOrigem) LIKE LOWER(CONCAT('%', :busca, '%')) " +
	           "     OR LOWER(v.cidadeDestino) LIKE LOWER(CONCAT('%', :busca, '%'))) " +
	           "AND (:dataInicial IS NULL OR v.dataViagem >= :dataInicial) " +
	           "AND (:dataFinal IS NULL OR v.dataRetorno <= :dataFinal) " +
	           "AND (:status IS NULL OR v.status = :status)")
	    List<Viagem> buscarComFiltros(
	        @Param("busca") String busca,
	        @Param("dataInicial") LocalDate dataInicial,
	        @Param("dataFinal") LocalDate dataFinal,
	        @Param("status") StatusViagem status
	    );

}
