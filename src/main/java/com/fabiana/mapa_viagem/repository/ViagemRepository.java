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
	 
	// Card 1: Viagens Agendadas (status AGENDADA, próximos 7 dias)
	    @Query("SELECT COUNT(v) FROM Viagem v WHERE v.status = :status " +
	           "AND v.dataViagem BETWEEN :hoje AND :seteDiasDepois")
	    long contarAgendadasProximosDias(
	        @Param("status") StatusViagem status,
	        @Param("hoje") LocalDate hoje,
	        @Param("seteDiasDepois") LocalDate seteDiasDepois
	    );
	    
	 // Card 2: Viagens Hoje (independente do status, só pela data)
	    @Query("SELECT COUNT(v) FROM Viagem v WHERE v.dataViagem = :hoje")
	    long contarViagensHoje(@Param("hoje") LocalDate hoje);

	    // Card 4: Viagens Finalizada (status FINALIZADA, dentro do mês corrente)
	    @Query("SELECT COUNT(v) FROM Viagem v WHERE v.status = :status " +
	           "AND v.dataViagem BETWEEN :inicioMes AND :fimMes")
	    long contarFinalizadasNoMes(
	        @Param("status") StatusViagem status,
	        @Param("inicioMes") LocalDate inicioMes,
	        @Param("fimMes") LocalDate fimMes
	    );


}
