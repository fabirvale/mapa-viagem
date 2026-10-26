package com.fabiana.mapa_viagem.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.fabiana.mapa_viagem.dto.PontoGraficoDTO;
import com.fabiana.mapa_viagem.enums.StatusViagem;
import com.fabiana.mapa_viagem.model.Viagem;

public interface ViagemRepository extends JpaRepository<Viagem, Long>{
	
     //Checar se existe duplicidade no cadastro	
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
	           "AND (:dataFinal IS NULL OR v.dataViagem <= :dataFinal) " +
	           "AND (:status IS NULL OR v.status = :status) " +
			   "AND (:motoristaId IS NULL OR v.motorista.id = :motoristaId)")
	    List<Viagem> buscarComFiltros(
	        @Param("busca") String busca,
	        @Param("dataInicial") LocalDate dataInicial,
	        @Param("dataFinal") LocalDate dataFinal,
	        @Param("status") StatusViagem status,
	        @Param("motoristaId") Long motoristaId
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
	    
	    
	    //Resumo diárias
	    @Query("""
	    	    SELECT v FROM Viagem v
	    	    WHERE v.motorista.id = :motoristaId
	    	      AND v.status = :status
	    	      AND v.dataViagem BETWEEN :inicio AND :fim
	    	    ORDER BY v.dataViagem
	    	    """)
	    	List<Viagem> buscarFinalizadasDoMotorista(@Param("motoristaId") Long motoristaId,
	    	                                          @Param("status") StatusViagem status,
	    	                                          @Param("inicio") LocalDate inicio,
	    	                                          @Param("fim") LocalDate fim);

	    	// viagens ainda abertas no período (alimenta o aviso amarelo)
	    	long countByMotoristaIdAndStatusNotInAndDataViagemBetween(Long motoristaId,
	    	                                                          Collection<StatusViagem> status,
	    	                                                          LocalDate inicio,
	    	                                                          LocalDate fim);
	    	
	    	
	    	//Gráficos
	    	@Query("""
	    		    SELECT new com.fabiana.mapa_viagem.dto.PontoGraficoDTO(m.nome, SUM(p.valorDiaria))
	    		    FROM Viagem v
	    		    JOIN v.motorista m
	    		    JOIN v.pagamentoDiaria p
	    		    WHERE v.status = :status
	    		      AND v.dataViagem BETWEEN :inicio AND :fim
	    		    GROUP BY m.id, m.nome
	    		    ORDER BY SUM(p.valorDiaria) DESC
	    		    """)
	    		List<PontoGraficoDTO> diariasPorMotorista(@Param("status") StatusViagem status,
	    		                                          @Param("inicio") LocalDate inicio,
	    		                                          @Param("fim") LocalDate fim);
	    	
	    	@Query("""
	    		    SELECT new com.fabiana.mapa_viagem.dto.PontoGraficoDTO(p.tipoDiaria, COUNT(p))
	    		    FROM Viagem v
	    		    JOIN v.pagamentoDiaria p
	    		    WHERE v.status = :status
	    		      AND v.dataViagem BETWEEN :inicio AND :fim
	    		    GROUP BY p.tipoDiaria
	    		    ORDER BY COUNT(p) DESC
	    		    """)
	    		List<PontoGraficoDTO> viagensPorTipoDiaria(@Param("status") StatusViagem status,
	    		                                           @Param("inicio") LocalDate inicio,
	    		                                           @Param("fim") LocalDate fim);
	    	
	    	@Query("""
	    		    SELECT new com.fabiana.mapa_viagem.dto.PontoGraficoDTO(v.status, COUNT(v))
	    		    FROM Viagem v
	    		    WHERE v.dataViagem BETWEEN :inicio AND :fim
	    		    GROUP BY v.status
	    		    ORDER BY COUNT(v) DESC
	    		    """)
	    		List<PontoGraficoDTO> viagensPorStatus(@Param("inicio") LocalDate inicio,
	    		                                       @Param("fim") LocalDate fim);
	    	
	    	
	    	@Query("""
	    		    SELECT YEAR(v.dataViagem), MONTH(v.dataViagem), v.status, COUNT(v)
	    		    FROM Viagem v
	    		    WHERE v.status IN (:status)
	    		      AND v.dataViagem BETWEEN :inicio AND :fim
	    		    GROUP BY YEAR(v.dataViagem), MONTH(v.dataViagem), v.status
	    		    """)
	    		List<Object[]> viagensPorMesEStatus(@Param("status") Collection<StatusViagem> status,
	    		                                    @Param("inicio") LocalDate inicio,
	    		                                    @Param("fim") LocalDate fim);
	    		
	    	@Query("""
	    			    SELECT DISTINCT v FROM Viagem v
	    			    JOIN FETCH v.veiculo
	    			    JOIN FETCH v.motorista
	    			    JOIN FETCH v.ocorrencias o
	    			    WHERE o.data BETWEEN :inicio AND :fim
	    			    """)
	    			List<Viagem> viagensComOcorrenciasNoPeriodo(@Param("inicio") LocalDate inicio,
	    			                                            @Param("fim") LocalDate fim);	


}
