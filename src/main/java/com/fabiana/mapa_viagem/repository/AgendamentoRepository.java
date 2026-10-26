package com.fabiana.mapa_viagem.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.fabiana.mapa_viagem.dto.PontoGraficoDTO;
import com.fabiana.mapa_viagem.enums.StatusAgendamento;
import com.fabiana.mapa_viagem.model.Agendamento;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {
	
	// Busca um agendamento por paciente e viagem
    Optional<Agendamento> findByPacienteIdAndViagemId(Long pacienteId, Long viagemId);
    
    //Busca um agendamento por paciente e data de atendimento
    Optional<Agendamento> findByPacienteIdAndDataAtendimento(Long pacienteId,LocalDate dataAtendimento);
    
    //Busca agendamento por viagem
    List<Agendamento> findByViagemId(Long viagemId);
  
    //Verifica se existe ao menos um agendamento, retorna boolean
    boolean existsByViagemId(Long viagemId);
    
    //Card3 : Número de pacientes
    @Query("SELECT COUNT(a) FROM Agendamento a WHERE a.viagem.dataViagem = :hoje")
    long contarPacientesAgendadosHoje(@Param("hoje") LocalDate hoje);
    
    //Gráfico
    @Query("""
    	    SELECT YEAR(a.dataAtendimento), MONTH(a.dataAtendimento), COUNT(a)
    	    FROM Agendamento a
    	    WHERE a.status = :status
    	      AND a.dataAtendimento BETWEEN :inicio AND :fim
    	    GROUP BY YEAR(a.dataAtendimento), MONTH(a.dataAtendimento)
    	    """)
    	List<Object[]> cancelamentosPorMes(@Param("status") StatusAgendamento status,
    	                                   @Param("inicio") LocalDate inicio,
    	                                   @Param("fim") LocalDate fim);
    	
    	@Query("""
    		    SELECT new com.fabiana.mapa_viagem.dto.PontoGraficoDTO(m.descricao, COUNT(a))
    		    FROM Agendamento a
    		    JOIN a.motivoCancelamento m
    		    WHERE a.status = :status
    		      AND a.dataAtendimento BETWEEN :inicio AND :fim
    		    GROUP BY m.id, m.descricao
    		    ORDER BY COUNT(a) DESC
    		    """)
    		List<PontoGraficoDTO> cancelamentosPorMotivo(@Param("status") StatusAgendamento status,
    		                                             @Param("inicio") LocalDate inicio,
    		                                             @Param("fim") LocalDate fim);

    		@Query("""
    		    SELECT new com.fabiana.mapa_viagem.dto.PontoGraficoDTO(e.especialidade, COUNT(a))
    		    FROM Agendamento a
    		    JOIN a.tipoEspecialidade e
    		    WHERE a.status <> :statusExcluido
    		      AND a.dataAtendimento BETWEEN :inicio AND :fim
    		    GROUP BY e.id, e.especialidade
    		    ORDER BY COUNT(a) DESC
    		    """)
    		List<PontoGraficoDTO> atendimentosPorEspecialidade(@Param("statusExcluido") StatusAgendamento statusExcluido,
    		                                                   @Param("inicio") LocalDate inicio,
    		                                                   @Param("fim") LocalDate fim);	

}
